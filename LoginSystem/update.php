<?php
require "Database.php";

$db = new Database();

if (isset($_POST['username']) && isset($_POST['door']) && isset($_POST['newdoor'])) 
{
    if ($db->dbConnect())
    {
        if ($db->update("smartdoor_user", $_POST['username'],  $_POST['door'],  $_POST['newdoor']))
        {
            echo "Passcode has been changed";
        } 
        else echo "Passcode not match";
    } 
    else echo "Error: Database connection";
} 
else echo "Failed to changed passcode";    
?>