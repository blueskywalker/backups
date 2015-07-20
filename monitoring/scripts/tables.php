<?php
$datasource = $_GET['src'];
$servername = "gridmgmt";
$username = "root";
$password = "mysql";
$dbname = "collections";


// Create connection_status
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "show tables";

$result = $conn->query($sql);

if (!$result) {
	echo "DB error, could not list tables\n";
	echo "MySQL Error: " . mysql_error();
	exit;
}

$tables=array();
while($row = mysqli_fetch_row($result)) {
		array_push($tables,$row[0]);
}

$conn->close();
$output= array($dbname => $tables);
echo json_encode($output);
?>
