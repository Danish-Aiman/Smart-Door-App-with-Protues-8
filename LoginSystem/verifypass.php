<?php
require "Database.php";

$db = new Database();

if (isset($_POST['door']) && isset($_POST['username'])) 
{
    if ($db->dbConnect())
    {
        if ($db->verifypass("smartdoor_user", $_POST['door'],  $_POST['username']))
        {
            echo "Door Unlocked";
        } 
        else echo "Wrong Passcode";
    } 
    else echo "Error: Database connection";
} 
else echo "Failed to unlocked door";    
?>