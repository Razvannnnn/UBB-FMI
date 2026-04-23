<?php
session_start();
require 'db.php';

if (!isset($_SESSION['csrf'])) {
    $_SESSION['csrf'] = bin2hex(random_bytes(32));
}

$stmt = $pdo->query("
    SELECT DISTINCT localitate_plecare AS localitate FROM trenuri
    UNION
    SELECT DISTINCT localitate_sosire AS localitate FROM trenuri
    ORDER BY localitate
");
$localitati = $stmt->fetchAll(PDO::FETCH_COLUMN);
?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Cautare Trenuri</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        label, select, input[type="submit"] { display: block; margin-top: 10px; }
    </style>
</head>
<body>
    <h1>Cautare trenuri</h1>
    <form action="search.php" method="POST">
        <label>Plecare:
            <select name="from" required>
                <option value="">-- selecteaza --</option>
                <?php foreach ($localitati as $loc): ?>
                    <option value="<?= htmlspecialchars($loc) ?>"><?= htmlspecialchars($loc) ?></option>
                <?php endforeach; ?>
            </select>
        </label>
        <label>Sosire:
            <select name="to" required>
                <option value="">-- selecteaza --</option>
                <?php foreach ($localitati as $loc): ?>
                    <option value="<?= htmlspecialchars($loc) ?>"><?= htmlspecialchars($loc) ?></option>
                <?php endforeach; ?>
            </select>
        </label>
        <label>
            <input type="checkbox" name="allow_connections"> Permite curse cu legaturi
        </label>
        <input type="hidden" name="csrf_token" value="<?= htmlspecialchars($_SESSION['csrf']) ?>">
        <input type="submit" value="Caută">
    </form>
</body>
</html>
