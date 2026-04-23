<?php
header('Content-Type: application/json');

$conn = new mysqli('localhost', 'root', '', 'lab7');
if ($conn->connect_error) {
  echo json_encode([]);
  exit;
}

$fields = ['producator','procesor','memorie','hdd','placa_video'];
$where = [];
$params = [];
$types = '';

foreach ($fields as $f) {
  if (!empty($_GET[$f])) {
    $where[] = "`$f` = ?";
    $params[] = $_GET[$f];
    $types .= 's';
  }
}

$sql = "SELECT * FROM produse";
if ($where) {
  $sql .= " WHERE " . implode(' AND ', $where);
}

$stmt = $conn->prepare($sql);

if ($params) {
  $stmt->bind_param($types, ...$params);
}

$stmt->execute();
$res = $stmt->get_result();

$produse = [];
while ($row = $res->fetch_assoc()) {
  $produse[] = $row;
}

echo json_encode($produse);
$conn->close();
