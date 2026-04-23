<?php
require 'config.php';
require_login();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        die('Token CSRF invalid.');
    }

    $id_poze = (int)($_POST['id_poze'] ?? 0);

    if ($id_poze <= 0) {
        die('ID poza invalid.');
    }

    // Verif poza exista si apartnie user
    $stmt = $pdo->prepare("SELECT nume_fisier FROM poze WHERE id = ? AND utilizator_id = ?");
    $stmt->execute([$id_poze, $_SESSION['utilizator_id']]);
    $poza = $stmt->fetch();

    if (!$poza) {
        die('Poză inexistentă sau acces nepermis.');
    }

    $fisier = __DIR__ . '/uploads/' . $poza['nume_fisier'];
    if (file_exists($fisier)) {
        unlink($fisier);
    }

    $stmt = $pdo->prepare("DELETE FROM poze WHERE id = ?");
    $stmt->execute([$id_poze]);

    header('Location: upload_poze.php');
    exit;
} else {
    header('Location: upload_poze.php');
    exit;
}
