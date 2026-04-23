<?php
$con = mysqli_connect("localhost", "root", "", "lab7");
if (!$con) {
    die('Could not connect: ' . mysqli_connect_error());
}

$result = mysqli_query($con, "SELECT id FROM angajati");
$ids = [];
while ($row = mysqli_fetch_assoc($result)) {
    $ids[] = $row['id'];
}

echo json_encode($ids);
mysqli_close($con);
?>
