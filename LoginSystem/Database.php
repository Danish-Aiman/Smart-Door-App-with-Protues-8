<?php
require 'RSA_Encryption.php';
//require 'AES_Encryption.php';

class Database
{
    public $connect;
    public $data;
    private $sql;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
    }

    function dbConnect()
    {
        $this->connect = pg_connect("host=localhost port=5432 dbname=smartdoor_user user=postgres password=rojak123");
        return $this->connect;
    }

    function prepareData($data)
    {
        return pg_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    
  function encrypt_aes($message, $encryption_key){
    $key = hex2bin($encryption_key);
    $nonceSize = openssl_cipher_iv_length('aes-256-ctr');
    $nonce = openssl_random_pseudo_bytes($nonceSize);
    $ciphertext = openssl_encrypt(
      $message, 
      'aes-256-ctr', 
      $key,
      OPENSSL_RAW_DATA,
      $nonce
    );
    return base64_encode($nonce.$ciphertext);
  }
  function decrypt_aes($message,$encryption_key){
    $key = hex2bin($encryption_key);
    $message = base64_decode($message);
    $nonceSize = openssl_cipher_iv_length('aes-256-ctr');
    $nonce = mb_substr($message, 0, $nonceSize, '8bit');
    $ciphertext = mb_substr($message, $nonceSize, null, '8bit');
    $plaintext= openssl_decrypt(
      $ciphertext, 
      'aes-256-ctr', 
      $key,
      OPENSSL_RAW_DATA,
      $nonce
    );
    return $plaintext;
  }

    
    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $rsa = new RSA_Encryption();
        //$aes = new AES_Encryption();

        //Aes private key
        $aes_private_secret_key = '1f4276388ad3214c873428dbef42243f';

        //Rsa private key
        $rsa_privatekey = $rsa->private_key;


        $this->sql = "select * from " . $table . " where username = '" . $username . "'";
        $result = pg_query($this->connect, $this->sql);
        $row = pg_fetch_assoc($result);
        if (pg_num_rows($result) != 0) 
        {
            $dbusername = $row['username'];
            $dbpassword = $row['password'];

            //Decrypt data using RSA algorithm
            $first_ps = $rsa->rsa_decrypt($dbpassword, $rsa_privatekey);

            //Decrypt data using AES algorithm
            $second_ps = $this->decrypt_aes($first_ps, $aes_private_secret_key);

            if ($dbusername == $username && $password == $second_ps) 
            {
                $login = true;
            } 
            else $login = false;
        } 
        else $login = false;

        return $login;
    }

    
    function signUp($table, $username, $password, $door)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        $rsa = new RSA_Encryption();
        //$aes = new AES_Encryption();

        //Aes private key
        $aes_private_secret_key = '1f4276388ad3214c873428dbef42243f';

        //Rsa private key
        $rsa_publickey = $rsa->public_key;
        
       //Encrypt data using AES algorithm
        $first_encrypt = $this->encrypt_aes($password, $aes_private_secret_key);

        //Encrypt data using RSA algorithm
        $second_encrypt = $rsa->rsa_encrypt($first_encrypt, $rsa_publickey);
        //$second_encrypt = openssl_public_encrypt($first_encrypt, $encrypt, $rsa_publickey);
    
        $door = $this->prepareData($door);
        $this->sql = "INSERT INTO  " . $table . " (username, password, door_passcode) VALUES ('" . $username . "','" . $second_encrypt . "','" . $door . "')";
        if (pg_query($this->connect, $this->sql)) 
        {
            return true;
        } 
        else return false;
    }

        
    function update ($table, $username, $door, $newdoor)
    {
        $username = $this->prepareData($username);
        $door = $this->prepareData($door);
        $this->sql = "update ". $table. " set door_passcode = ". $newdoor . " where username = '". $username . "' and ". " door_passcode = '". $door . "'";
        if (pg_query($this->connect, $this->sql)) 
        {
            return true;
        }
        else return false;
    }

    function verifypass ($table, $door)
    {
        $door = $this->prepareData($door);
        $this->sql = "select * from " . $table . " where door_passcode = '" . $door . "'";
        $result = pg_query($this->connect, $this->sql);
        $row = pg_fetch_assoc($result);
        if (pg_num_rows($result) != 0) 
        {
            $dbdoor = $row['door_passcode'];
            if($dbdoor == $door)
            {
                $unlock = true;
                $sql = "update ". $table. " set door_status = date_trunc('second',now()) where door_passcode = '". $door . "'";
                if (pg_query($this->connect, $sql)) 
                {
                    return true;
                }
            }
            else $unlock = false;
        }
        else $unlock = false;

        return $unlock;
    }

    function delete ($table,  $username)
    {
        $username = $this->prepareData($username);
        $this->sql = "delete from " . $table . " where username = '" . $username . "'";
        if (pg_query($this->connect, $this->sql)) 
        {
            return true;
        }
        else return false;
    }

    function history ($table, $door)
    {
        $door = $this->prepareData($door);
        $this->sql = "select * from " . $table . " where door_passcode = '" . $door . "'";
        $result = pg_query($this->connect, $this->sql);
        $row = pg_fetch_assoc($result);
        
        if($result && pg_num_rows($result) > 0)
            {
                $dbdoor = $row['door_passcode'];
                if($dbdoor == $door)
                {
                    $status = $row['door_status'];
                    return $status;
                }
                else return false;
            }
            else return false;
        }
}
?>