<?php
require "Database.php";

$db = new Database();

if (isset($_POST['door'])) 
{
    if ($db->dbConnect())
    {
        if ($db->history("smartdoor_user", $_POST['door']))
        {
            echo $db->history('smartdoor_user', $_POST['door']);
        } 
        else echo "Wrong Passcode, Try Again";
    } 
    else echo "Error: Database connection";
} 
else echo "Failed to unlocked door";    
?>  