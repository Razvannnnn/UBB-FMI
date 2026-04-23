<?php
require 'config.php';

$username_nou = 'prof2';
$parola_noua = 'parola123';

$stmt = $pdo->prepare("SELECT id FROM profesori WHERE username = ?");
$stmt->execute([$username_nou]);
if ($stmt->fetch()) {
    die("Username-ul '$username_nou' exista deja in baza de date.");
}

$hash_parola = password_hash($parola_noua, PASSWORD_DEFAULT);


$stmt = $pdo->prepare("INSERT INTO profesori (username, parola) VALUES (?, ?)");
if ($stmt->execute([$username_nou, $hash_parola])) {
    echo "Profesorul '$username_nou' a fost adaugat cu succes cu parola '$parola_noua'.";
} else {
    echo "Eroare la adaugarea profesorului.";
}
