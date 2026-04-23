<?php
require 'config.php';

$username_nou = 'user2';
$parola_noua = 'parola123';

$stmt = $pdo->prepare("SELECT id FROM utilizatori WHERE username = ?");
$stmt->execute([$username_nou]);
if ($stmt->fetch()) {
    die("Username-ul '$username_nou' exista deja în baza de date.");
}

$hash_parola = password_hash($parola_noua, PASSWORD_DEFAULT);

$stmt = $pdo->prepare("INSERT INTO utilizatori (username, parola) VALUES (?, ?)");
if ($stmt->execute([$username_nou, $hash_parola])) {
    echo "Utilizatorul '$username_nou' a fost adaugat cu succes cu parola '$parola_noua'.";
} else {
    echo "Eroare la adaugarea utilizatorului.";
}
