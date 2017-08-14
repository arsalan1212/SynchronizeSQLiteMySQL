<?php

$userName="root";
$server ="localhost";
$password ="";
$db_name ="sqlite_sync";

$connection = mysqli_connect($server,$userName,$password,$db_name);

$state ='';

if($connection){
	
	$name = $_POST['name'];

	$query = "INSERT INTO info VALUES ('','$name')";

     $result = mysqli_query($connection,$query);

     if($result){

     	$state ="OK";
     }
     else{
     	$state = "failed";
     }

}
else{

	$state="failed";
}

echo json_encode(array("response"=>$state));

?>