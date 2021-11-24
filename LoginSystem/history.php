<?php
require "Database.php";

$db = new Database();

if (isset($_POST['door']) && isset($_POST['username'])) 
{
    if ($db->dbConnect())
    {
        if ($db->history("smartdoor_user", $_POST['door'],  $_POST['username']))
        {
            echo $db->history('smartdoor_user', $_POST['door'],  $_POST['username']);
        } 
        else echo "Wrong Passcode, Try Again";
    } 
    else echo "Error: Database connection";
} 
else echo "Failed to unlocked door";    
?>  