<?php
require 'config.php';

$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        $errors[] = "Token CSRF invalid.";
    } else {
        $username = trim($_POST['username'] ?? '');
        $parola = trim($_POST['parola'] ?? '');

        if (!$username || !$parola) {
            $errors[] = "Completati username si parola.";
        } else {
            $stmt = $pdo->prepare("SELECT * FROM utilizatori WHERE username = ?");
            $stmt->execute([$username]);
            $utilizator = $stmt->fetch();

            if ($utilizator && password_verify($parola, $utilizator['parola'])) {
                session_regenerate_id(true);
                $_SESSION['utilizator_id'] = $utilizator['id'];
                $_SESSION['utilizator_username'] = $utilizator['username'];
                header('Location: upload_poze.php');
                exit;
            } else {
                $errors[] = "Date de autentificare invalide.";
            }
        }
    }
}

$csrf_token = generate_csrf_token();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Login utilizator</title></head>
<body>
<h1>Autentificare utilizator</h1>

<?php if ($errors): ?>
    <ul style="color:red;">
        <?php foreach ($errors as $e) echo "<li>" . htmlspecialchars($e) . "</li>"; ?>
    </ul>
<?php endif; ?>

<form method="post" action="login.php" novalidate>
    <input type="hidden" name="csrf_token" value="<?= htmlspecialchars($csrf_token) ?>">
    <label>Username: <input type="text" name="username" required></label><br><br>
    <label>Parola: <input type="password" name="parola" required></label><br><br>
    <button type="submit">Autentificare</button>
</form>

</body>
</html>
