<?php

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL,"http://www.google.com");
    curl_exec($curl);
    curl_close($curl);
?>