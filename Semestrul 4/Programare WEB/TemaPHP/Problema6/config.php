<?php
session_start();
$pdo = new PDO('mysql:host=localhost;dbname=comentarii;charset=utf8mb4', 'root', '');
$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

function generate_csrf_token() {
    if (empty($_SESSION['csrf'])) {
        $_SESSION['csrf'] = bin2hex(random_bytes(32));
    }
    return $_SESSION['csrf'];
}

function verify_csrf_token($token) {
    return hash_equals($_SESSION['csrf'] ?? '', $token);
}

function require_admin_login() {
    if (!isset($_SESSION['admin_id'])) {
        header('Location: login.php');
        exit;
    }
}
?>
