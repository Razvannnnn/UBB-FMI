<?php
header('Content-Type: application/json');

$root = 'C:\\xampp';

$requestedPath = isset($_GET['path']) ? $_GET['path'] : $root;

if(!is_dir($requestedPath)) {
    echo json_encode(['error' => 'Director inexistent']);
    exit;
}

$items = scandir($requestedPath);
$result = [];

foreach($items as $item) {
    if($item === '.' || $item === '..') continue;

    $fullPath = $requestedPath . DIRECTORY_SEPARATOR . $item;

    $result[] = [
    'name' => $item,
    'type' => is_dir($fullPath) ? 'directory' : 'file',
    'path' => $fullPath
];
}

echo json_encode($result);
