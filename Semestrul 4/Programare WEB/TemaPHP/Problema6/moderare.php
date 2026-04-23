<?php
require 'config.php';
require_admin_login();

if ($_SERVER['REQUEST_METHOD'] === 'POST' && verify_csrf_token($_POST['csrf_token'] ?? '')) {
    $id = (int)$_POST['id'];
    if (isset($_POST['aproba'])) {
        $pdo->prepare("UPDATE comentarii SET aprobat = 1 WHERE id = ?")->execute([$id]);
    } elseif (isset($_POST['sterge'])) {
        $pdo->prepare("DELETE FROM comentarii WHERE id = ?")->execute([$id]);
    }
}

$comentarii = $pdo->query("SELECT * FROM comentarii WHERE aprobat = 0 ORDER BY data_adaugare DESC")->fetchAll();
$csrf_token = generate_csrf_token();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Moderare comentarii</title></head>
<body>
<h1>Moderare comentarii</h1>
<p><a href="logout.php">Deconectare</a></p>

<?php if (!$comentarii): ?>
    <p>Nu exista comentarii de moderat.</p>
<?php else: ?>
    <?php foreach ($comentarii as $c): ?>
        <form method="post" action="moderare.php" style="margin-bottom:20px;">
            <strong><?= htmlspecialchars($c['nume']) ?>:</strong><br>
            <?= nl2br(htmlspecialchars($c['text'])) ?><br>
            <small><?= $c['data_adaugare'] ?></small><br>
            <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
            <input type="hidden" name="id" value="<?= $c['id'] ?>">
            <button name="aproba" type="submit">Aproba</button>
            <button name="sterge" type="submit">Sterge</button>
        </form>
    <?php endforeach; ?>
<?php endif; ?>
</body>
</html>
