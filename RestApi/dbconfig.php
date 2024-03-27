<?php

$DBhost = "localhost";//you know the drill :))
$DBuser = "root";//you know the drill :))
$DBpassword ="";//you know the drill :))
$DBname="test"; //you know the drill :))

$conn = mysqli_connect($DBhost, $DBuser, $DBpassword, $DBname); 

if(!$conn){
	die("Connection failed: " . mysqli_connect_error());
}

?> 
