<?php

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

    
    function logIn($table, $username, $password)
    {
        $username = $this->prepareData($username);
        $password = $this->prepareData($password);
        //$private_secret_key = '1f4276388ad3214c873428dbef42243f';
        //$real_ps = $this->decrypt($password, $private_secret_key);
        $this->sql = "select * from " . $table . " where username = '" . $username . "'";
        $result = pg_query($this->connect, $this->sql);
        $row = pg_fetch_assoc($result);
        if (pg_num_rows($result) != 0) 
        {
            $dbusername = $row['username'];
            $dbpassword = $row['password'];
            //$real_ps = $this->decrypt($dbpassword, $private_secret_key)
            if ($dbusername == $username && $password == $dbpassword) 
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
        //$private_secret_key = '1f4276388ad3214c873428dbef42243f';
        //$encrypted = $this->encrypt($password,$private_secret_key);
        $door = $this->prepareData($door);
        $this->sql = "INSERT INTO  " . $table . " (username, password, door_passcode) VALUES ('" . $username . "','" . $password . "','" . $door . "')";
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