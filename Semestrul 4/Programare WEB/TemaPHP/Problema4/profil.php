<?php
require 'config.php';

$id_utilizator = (int)($_GET['id'] ?? 0);
if ($id_utilizator <= 0) {
    die('ID utilizator invalid.');
}

// Obtine username
$stmt = $pdo->prepare("SELECT username FROM utilizatori WHERE id = ?");
$stmt->execute([$id_utilizator]);
$utilizator = $stmt->fetch();

if (!$utilizator) {
    die('Utilizator inexistent.');
}

// Obtine pozele
$stmt = $pdo->prepare("SELECT * FROM poze WHERE utilizator_id = ? ORDER BY data_upload DESC");
$stmt->execute([$id_utilizator]);
$poze = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Profil utilizator</title></head>
<body>
<h1>Profilul lui <?= htmlspecialchars($utilizator['username']) ?></h1>

<?php if (count($poze) === 0): ?>
    <p>Acest utilizator nu are încarcate poze.</p>
<?php else: ?>
    <?php foreach ($poze as $p): ?>
        <div style="margin-bottom:10px;">
            <img src="uploads/<?= htmlspecialchars($p['nume_fisier']) ?>" alt="Poză" style="max-width:200px;">
        </div>
    <?php endforeach; ?>
<?php endif; ?>

<p><a href="upload_poze.php">Incarca poze</a> | <a href="logout.php">Deconectare</a></p>
</body>
</html>
        