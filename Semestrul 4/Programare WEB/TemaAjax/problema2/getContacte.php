<?php
$con = mysqli_connect("localhost", "root", "", "lab7");
if (!$con) {
    die('Could not connect: ' . mysqli_connect_error());
}

$offset = intval($_GET['offset']);
$limit = intval($_GET['limit']);

$sql = "SELECT nume, prenume, telefon, email FROM contacte LIMIT $limit OFFSET $offset";
$result = mysqli_query($con, $sql);

$data = array();
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

echo json_encode(['rows' => $data]);
mysqli_close($con);
?>
