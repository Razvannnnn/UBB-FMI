<?php
require 'config.php';
$username = 'admin';
$parola = password_hash('parola123', PASSWORD_DEFAULT);
$pdo->prepare("INSERT INTO admini (username, parola) VALUES (?, ?)")->execute([$username, $parola]);
echo "Admin adaugat.";
