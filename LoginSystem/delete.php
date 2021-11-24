<?php
require "Database.php";

$db = new Database();

if (isset($_POST['username'])) 
{
    if ($db->dbConnect())
    {
        if ($db->delete("smartdoor_user", $_POST['username']))
        {
            echo "Successfully Delete Account";
        } 
        else echo "Failed to Delete Account";
    } 
    else echo "Error: Database connection";
} 
else echo "Please Try Again";    
?>
