<?php
require 'config.php';

$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        $errors[] = "Token CSRF invalid.";
    } else {
        $user = trim($_POST['username']);
        $pass = trim($_POST['parola']);

        $stmt = $pdo->prepare("SELECT * FROM admini WHERE username = ?");
        $stmt->execute([$user]);
        $admin = $stmt->fetch();

        if ($admin && password_verify($pass, $admin['parola'])) {
            session_regenerate_id(true);
            $_SESSION['admin_id'] = $admin['id'];
            $_SESSION['admin_user'] = $admin['username'];
            header("Location: moderare.php");
            exit;
        } else {
            $errors[] = "Credentiale invalide.";
        }
    }
}

$csrf_token = generate_csrf_token();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Login admin</title></head>
<body>
<h1>Autentificare administrator</h1>

<?php if ($errors): ?>
    <ul style="color:red;">
        <?php foreach ($errors as $e) echo "<li>" . htmlspecialchars($e) . "</li>"; ?>
    </ul>
<?php endif; ?>

<form method="post" action="login.php">
    <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
    <label>Username: <input type="text" name="username" required></label><br><br>
    <label>Parola: <input type="password" name="parola" required></label><br><br>
    <button type="submit">Autentificare</button>
</form>
</body>
</html>
