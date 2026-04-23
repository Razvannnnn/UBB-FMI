<?php
$con = mysqli_connect("localhost", "root", "", "lab7");
if (!$con) {
    die('Could not connect: ' . mysqli_connect_error());
}

$id = intval($_POST['id']);
$nume = mysqli_real_escape_string($con, $_POST['nume']);
$prenume = mysqli_real_escape_string($con, $_POST['prenume']);
$telefon = mysqli_real_escape_string($con, $_POST['telefon']);
$email = mysqli_real_escape_string($con, $_POST['email']);

$sql = "UPDATE angajati SET nume='$nume', prenume='$prenume', telefon='$telefon', email='$email' WHERE id=$id";

if (mysqli_query($con, $sql)) {
    echo "OK";
} else {
    http_response_code(500);
    echo "Error updating record: " . mysqli_error($con);
}

mysqli_close($con);
?>
