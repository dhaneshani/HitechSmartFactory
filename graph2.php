<?php
	//header('Content-type: application/json');
	//http://localhost/graph2.php
	$requestBody = file_get_contents('php://input');                         
        $requestBodyjson = json_decode($requestBody) or die("Could not decode JSON");       
        $tag = $requestBodyjson->Tag;
	$con=mysqli_connect('localhost','root','','Hitech_Smart_Factory'); 
	$sql="SELECT * FROM Tags WHERE Name = '". $tag. "'";
	$result=mysqli_query($con,$sql);
	$row=mysqli_fetch_array($result,MYSQLI_ASSOC);
	if($row["Graph"]=="1"){
		viewGraph($tag);
	}else{
		echo "Sorry!! No Data to Show";
		echo '<html><iframe "Sorry!! No Data to Show" height="200" width="600" frameborder="0"></iframe></html>';
	}   
	function viewGraph($value) {
		$url = "http://localhost:3000/dashboard-solo/db/".$value."?refresh=1s&orgId=1&panelId=1";
		echo '<html><iframe src='.$url.'  height="200" width="600" frameborder="0"></iframe></html>';
	}
?> 
