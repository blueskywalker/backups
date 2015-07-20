<?php

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL,"http://10.200.24.9/api/rest/v1/post/perday?query=*:*&startDate=2015-02-01&endDate=2015-03-01&timezone=UTC&dataSrcs=NW");
    curl_exec($curl);
    curl_close($curl);
?>
