<?php
$collection=$_GET['src'];
$start = $_GET['start'];
$end = $_GET['end'];
$servername = "gridmgmt";
$username = "root";
$password = "mysql";
$dbname = "collections";



// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

// Check connection
if (mysqli_connect_errno()) {
    die("Connection failed: " . mysqli_connect_error());
}

$sql = "CALL period_index('{$collection}',{$start},{$end})";

$result = mysqli_query($conn,$sql);

if (!$result) {
	echo "DB error, could not list tables\n";
	echo "MySQL Error: " . mysql_error();
	exit;
}

$output=array();
while($row = mysqli_fetch_array($result)) {
    array_push($output,array('date' => $row['epoch']*1000,
        'count' => $row['numFound'],
        'qtime' => $row['qtime']));
}
mysqli_close($conn);
echo json_encode($output);

?>
