<?php

class RSA_Encryption
{

public $public_key = 
'-----BEGIN PUBLIC KEY-----
MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK8t5QsjF+hKPY/5/hV+SMYWst+A9BK4
54KDwCzK9gl6TSPpSlFYHYyN55AOeeZk1UVet/d0kSV+zISmWYjaypUCAwEAAQ==
-----END PUBLIC KEY-----';

public $private_key = 
'-----BEGIN RSA PRIVATE KEY-----
MIIBPAIBAAJBAK8t5QsjF+hKPY/5/hV+SMYWst+A9BK454KDwCzK9gl6TSPpSlFY
HYyN55AOeeZk1UVet/d0kSV+zISmWYjaypUCAwEAAQJBAInIxF1lJdm8Av3qeHG5
WU4M7nYjPFY5f0ZFkHLq1GgQrvBZReICaDvpZA8as7V98XdQxUTWecKeDNEMUQa9
M8UCIQDj+qg9dT5aCpI5VU6KsIVFNWq8wuoakKjhm2OMldlKZwIhAMS15RZb5edp
rgHUlpxEQKjutk+x0Cmv42JZ6CZLH12jAiBXJY4CUDsReFEFEZMAeRG4rv6qQWfz
17B27UtUhkOVqQIhALTFwCC7FV2ENeACwOIcqxtUPICilP0qYZlprDk8wjdnAiEA
u1ZewPFiRfD/AZJXPRoTS4VuXpdVVSGZyuGZj8ZnJBw=
-----END RSA PRIVATE KEY-----';

    function rsa_encrypt($msg, $public_key)
    {
        if (openssl_public_encrypt($msg, $encrypt, $public_key))
            $msg = base64_encode($encrypt);
        else
            throw new Exception('Unable to encrypt data. Perhaps it is bigger than the key size?');

        return $msg;
    }

    function rsa_decrypt($msg, $private_key)
    {
        if (openssl_private_decrypt(base64_decode($msg), $decrypted, $private_key))
            $msg = $decrypted;
        else
            $msg = ' ';

        return $msg;
    }

}
?>