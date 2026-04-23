<?php
$rootDir = realpath('C:\\xampp');

$requestedPath = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = realpath($rootDir . DIRECTORY_SEPARATOR . $requestedPath);

if($fullPath === false || strpos($fullPath, $rootDir) !== 0) {
    header('HTTP/1.1 403 Forbidden');
    echo 'Acces nepermis';
    exit;
}

if(!is_file($fullPath)) {
    header('HTTP/1.1 404 Not Found');
    echo 'Fisier inexistent';
    exit;
}

header('Content-Type: text/plain');
echo file_get_contents($fullPath);
