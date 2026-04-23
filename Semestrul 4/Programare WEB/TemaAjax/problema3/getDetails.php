<?php
$con = mysqli_connect("localhost", "root", "", "lab7");
if (!$con) {
    die('Could not connect: ' . mysqli_connect_error());
}

$id = intval($_GET['id']);

$result = mysqli_query($con, "SELECT nume, prenume, telefon, email FROM angajati WHERE id = $id LIMIT 1");
$data = mysqli_fetch_assoc($result);

echo json_encode($data);
mysqli_close($con);
?>
