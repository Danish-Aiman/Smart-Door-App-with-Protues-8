<?php
require "Database.php";

$db = new Database();

if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['door'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("smartdoor_user", $_POST['username'], $_POST['password'], $_POST['door'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
