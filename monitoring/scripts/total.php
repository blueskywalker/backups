<?php
$start = $_GET['start'];
$end = $_GET['end'];
$servername = "gridmgmt";
$username = "root";
$password = "mysql";
$dbname = "gridmon";


function sql_exec($sql) {
    // Create connection
    global $servername;
    global $username;
    global $password;
    global $dbname;

    $conn = mysqli_connect($servername, $username, $password, $dbname);

    // Check connection
    if (mysqli_connect_errno()) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $result = mysqli_query($conn,$sql);
    if (!$result) {
        echo "DB error, could not list tables\n";
        echo "MySQL Error: " . mysql_error();
        exit;
    }

    $output=array();
    while($row = mysqli_fetch_array($result)) {
		array_push($output,$row);
    }
    $result->close();
    mysqli_close($conn);
    return $output;
}


$output = sql_exec("CALL fetchTotal($start,$end)");
$output1 = sql_exec("CALL fetchSize($start,$end)");
$output2 = sql_exec("CALL fetchPost($start,$end)");

echo json_encode(array('docs'=> $output, 'size' => $output1,'post' => $output2));

?>
