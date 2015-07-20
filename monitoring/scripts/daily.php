<?php

$datasource = $_GET['src'];
$startDate = $_GET['start'];
$endDate = $_GET['end'];

if (!isset($datasource) or !isset($startDate) or !isset($endDate)) {
    exit();
}

$url = "http://10.200.24.9/api/rest/v1/post/perday?query=*:*&startDate={$startDate}&endDate={$endDate}&timezone=UTC&dataSrcs={$datasource}";

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_exec($ch);
curl_close($ch);

?>
