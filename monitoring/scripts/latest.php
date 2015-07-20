<?php
$datasource = $_GET['src'];
$servername = "gridmgmt";
$username = "root";
$password = "mysql";
$dbname = "collections";

function filter_source($table) {
    global $datasource;
    $tarray=explode("_",$table);
    return strcmp($tarray[1],$datasource)==0;
}

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

// Check connection
if (mysqli_connect_errno()) {
    die("Connection failed: " . mysqli_connect_error());
}

$sql = "show tables";
$result = mysqli_query($conn,$sql);

if (!$result) {
	echo "DB error, could not list tables\n";
	echo "MySQL Error: " . mysql_error();
	exit;
}

$tables=array();
while($row = mysqli_fetch_row($result)) {
		array_push($tables,$row[0]);
}

if (isset($datasource)) {
    $tables = array_values(array_filter($tables,"filter_source"));
}

$sql="";
$output=array();

foreach($tables as $table) {
	$sql = $sql . " CALL latest_index('{$table}');";
}

$results = mysqli_multi_query($conn,$sql);

$index=0;
$output=array();

if($results) {
	do  {
		$result=mysqli_store_result($conn);
		if($result) {
			$row=mysqli_fetch_array($result);
			$output = array_merge($output,array( $tables[$index] => array("timestamp" => $row['epoch'], "numfound" => $row['numFound'], "qtime" => $row['qtime'])));
			$index++;
		}
    } while(mysqli_next_result($conn) and $index < count($tables));
}

echo json_encode($output) . "\n";
mysqli_close($conn);

?>
