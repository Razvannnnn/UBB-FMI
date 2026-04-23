<?php
require 'config.php';

$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        $errors[] = "Token CSRF invalid.";
    } else {
        $nume = trim($_POST['nume'] ?? '');
        $text = trim($_POST['text'] ?? '');

        if (!$nume || !$text) {
            $errors[] = "Completati numele si comentariul.";
        } else {
            $stmt = $pdo->prepare("INSERT INTO comentarii (nume, text) VALUES (?, ?)");
            $stmt->execute([$nume, $text]);
        }
    }
}

$comentarii = $pdo->query("SELECT nume, text, data_adaugare FROM comentarii WHERE aprobat = 1 ORDER BY data_adaugare DESC")->fetchAll();
$csrf_token = generate_csrf_token();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Finala Champions League 2025</title>
</head>
<body>
<h1>Finala UEFA Champions League 2025: PSG vs Inter</h1>

<p><strong>Data:</strong> 31 mai 2025<br>
<strong>Locația:</strong> Allianz Arena, München</p>

<p>Paris Saint-Germain și Inter Milano se vor înfrunta în marea finală a UEFA Champions League 2025. Ambele echipe au avut parcursuri spectaculoase în competiție, eliminând adversari de top pentru a ajunge în ultimul act.</p>

<p>PSG, condusă de Kylian Mbappé, caută primul trofeu UEFA Champions League din istoria clubului, în timp ce Inter își propune să cucerească cel de-al patrulea titlu european. Finala promite să fie un meci intens, cu fotbal de înaltă clasă și emoție maximă.</p>

<p>Suporterii din întreaga lume așteaptă cu nerăbdare confruntarea, care va încheia sezonul european într-un mod spectaculos.</p>
</body>
</html>


<hr>
<h2>Comentarii:</h2>

<?php foreach ($comentarii as $c): ?>
    <div style="margin-bottom: 10px;">
        <strong><?= htmlspecialchars($c['nume']) ?>:</strong><br>
        <?= nl2br(htmlspecialchars($c['text'])) ?><br>
        <small><?= $c['data_adaugare'] ?></small>
    </div>
<?php endforeach; ?>

<hr>
<h2>Adauga comentariu</h2>

<?php if ($errors): ?>
    <ul style="color: red;">
        <?php foreach ($errors as $e) echo "<li>" . htmlspecialchars($e) . "</li>"; ?>
    </ul>
<?php endif; ?>

<form method="post" action="index.php">
    <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
    <label>Nume: <input type="text" name="nume" required></label><br><br>
    <label>Comentariu:<br>
        <textarea name="text" rows="5" cols="40" required></textarea>
    </label><br><br>
    <button type="submit">Trimite comentariul</button>
</form>
</body>
</html>
