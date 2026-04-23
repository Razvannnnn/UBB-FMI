<?php
require 'config.php';
require_login();

$stmt = $pdo->query("SELECT id, username FROM utilizatori ORDER BY username ASC");
$utilizatori = $stmt->fetchAll();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Lista utilizatori</title></head>
<body>
<h1>Lista utilizatori</h1>
<p><a href="upload_poze.php">Incarca poze</a> | <a href="logout.php">Deconectare</a></p>

<?php if (count($utilizatori) === 0): ?>
    <p>Nu exista utilizatori.</p>
<?php else: ?>
    <ul>
        <?php foreach ($utilizatori as $u): ?>
            <li><a href="profil.php?id=<?= (int)$u['id'] ?>"><?= htmlspecialchars($u['username']) ?></a></li>
        <?php endforeach; ?>
    </ul>
<?php endif; ?>
</body>
</html>
