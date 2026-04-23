<?php
header('Content-Type: application/json');

$root = realpath('C:/xampp/htdocs');

$relPath = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = realpath($root . DIRECTORY_SEPARATOR . $relPath);

if ($fullPath === false || strpos($fullPath, $root) !== 0) {
    echo json_encode(['error' => 'Acces nepermis']);
    exit;
}

if (!is_dir($fullPath)) {
    echo json_encode(['error' => 'Nu este un director valid']);
    exit;
}

$items = scandir($fullPath);
$result = [];

foreach ($items as $item) {
    if ($item === '.' || $item === '..') continue;
    $itemFullPath = $fullPath . DIRECTORY_SEPARATOR . $item;
    $itemRelPath = ltrim(str_replace($root, '', $itemFullPath), DIRECTORY_SEPARATOR);

    $result[] = [
        'name' => $item,
        'type' => is_dir($itemFullPath) ? 'directory' : 'file',
        'path' => $itemRelPath
    ];
}

echo json_encode($result);
