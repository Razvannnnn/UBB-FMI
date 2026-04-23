<?php
header('Content-Type: application/json');

$rootDir = realpath(__DIR__ . '/root');

$requestedPath = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = realpath($rootDir . DIRECTORY_SEPARATOR . $requestedPath);

if($fullPath === false || strpos($fullPath, $rootDir) !== 0) {
    echo json_encode(['error' => 'Acces nepermis']);
    exit;
}

if(!is_dir($fullPath)) {
    echo json_encode(['error' => 'Nu este director']);
    exit;
}

$items = scandir($fullPath);
$result = [];

foreach($items as $item) {
    if($item === '.' || $item === '..') continue;
    $itemPath = $fullPath . DIRECTORY_SEPARATOR . $item;
    $result[] = [
        'name' => $item,
        'type' => is_dir($itemPath) ? 'directory' : 'file',
        'path' => ltrim(str_replace($rootDir, '', $itemPath), DIRECTORY_SEPARATOR)
    ];
}

echo json_encode($result);
