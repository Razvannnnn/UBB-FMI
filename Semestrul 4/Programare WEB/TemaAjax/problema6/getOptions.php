<?php
header('Content-Type: application/json');

$conn = new mysqli('localhost', 'root', '', 'lab7');
if ($conn->connect_error) {
  echo json_encode([]);
  exit;
}

$field = $_GET['field'] ?? '';
$allowed = ['producator','procesor','memorie','hdd','placa_video'];

if (!in_array($field, $allowed)) {
  echo json_encode([]);
  exit;
}

$sql = "SELECT DISTINCT `$field` FROM produse ORDER BY `$field`";
$res = $conn->query($sql);

$options = [];
if ($res) {
  while ($row = $res->fetch_assoc()) {
    $options[] = $row[$field];
  }
}

echo json_encode($options);
$conn->close();
