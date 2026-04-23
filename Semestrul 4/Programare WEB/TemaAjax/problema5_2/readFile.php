<?php
$root = realpath('C:/xampp/htdocs');

$relPath = isset($_GET['path']) ? $_GET['path'] : '';
$fullPath = realpath($root . DIRECTORY_SEPARATOR . $relPath);

if ($fullPath === false || strpos($fullPath, $root) !== 0 || !is_file($fullPath)) {
    header('HTTP/1.1 403 Forbidden');
    echo 'Acces nepermis sau fisier inexistent';
    exit;
}

header('Content-Type: text/plain');
readfile($fullPath);
