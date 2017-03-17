<?php
include('GCMPushMessage.php');
	$gcm = new GCMPushMessage('AIzaSyC4gBMebYYxLNu-7icXore_FQjE0YHPGeU');
	$gcm->setDevices('/topics/global');
	$gcm->send("http://".$argv[1].":5000");