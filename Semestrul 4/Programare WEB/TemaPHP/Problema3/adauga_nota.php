<?php
require 'config.php';

if (empty($_SESSION['profesor_id'])) {
    header('Location: login.php');
    exit;
}

$errors = [];
$success = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        $errors[] = "Token CSRF invalid.";
    } else {
        $student_id = $_POST['student_id'] ?? '';
        $materie_id = $_POST['materie_id'] ?? '';
        $nota = $_POST['nota'] ?? '';

        if (!is_numeric($student_id) || !is_numeric($materie_id) || !is_numeric($nota)) {
            $errors[] = "Date invalide.";
        } elseif ($nota < 1 || $nota > 10) {
            $errors[] = "Nota trebuie sa fie intre 1 si 10.";
        } else {
            // Verific daca exista
            $stmt = $pdo->prepare("SELECT COUNT(*) FROM studenti WHERE id = ?");
            $stmt->execute([$student_id]);
            if ($stmt->fetchColumn() == 0) $errors[] = "Student invalid.";

            $stmt = $pdo->prepare("SELECT COUNT(*) FROM materii WHERE id = ?");
            $stmt->execute([$materie_id]);
            if ($stmt->fetchColumn() == 0) $errors[] = "Materie invalida.";

            if (!$errors) {
                // Verific daca prof a mai notat student
                $stmt = $pdo->prepare("SELECT id FROM note WHERE student_id = ? AND materie_id = ? AND profesor_id = ?");
                $stmt->execute([$student_id, $materie_id, $_SESSION['profesor_id']]);
                $nota_existenta = $stmt->fetch();

                if ($nota_existenta) {
                    // Update nota
                    $stmt = $pdo->prepare("UPDATE note SET nota = ? WHERE id = ?");
                    $stmt->execute([$nota, $nota_existenta['id']]);
                    $success = "Nota actualizata cu succes.";
                } else {
                    // Insert nota noua
                    $stmt = $pdo->prepare("INSERT INTO note (student_id, materie_id, profesor_id, nota) VALUES (?, ?, ?, ?)");
                    $stmt->execute([$student_id, $materie_id, $_SESSION['profesor_id'], $nota]);
                    $success = "Nota adaugata cu succes.";
                }
            }
        }
    }
}

$csrf_token = generate_csrf_token();

$studenti = $pdo->query("SELECT * FROM studenti ORDER BY nume")->fetchAll(PDO::FETCH_ASSOC);
$materii = $pdo->query("SELECT * FROM materii ORDER BY denumire")->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Adauga nota</title></head>
<body>
<h1>Adauga nota</h1>
<p>Profesor: <?= htmlspecialchars($_SESSION['profesor_username']) ?> | <a href="logout.php">Logout</a></p>

<?php if ($errors): ?>
    <ul style="color:red;">
        <?php foreach ($errors as $e) echo "<li>" . htmlspecialchars($e) . "</li>"; ?>
    </ul>
<?php endif; ?>

<?php if ($success): ?>
    <p style="color:green;"><?= htmlspecialchars($success) ?></p>
<?php endif; ?>

<form method="post" action="adauga_nota.php" novalidate>
    <input type="hidden" name="csrf_token" value="<?= $csrf_token ?>">
    
    <label>Student:
        <select name="student_id" required>
            <option value="">-- Selecteaza student --</option>
            <?php foreach ($studenti as $s): ?>
                <option value="<?= $s['id'] ?>"><?= htmlspecialchars($s['nume']) ?></option>
            <?php endforeach; ?>
        </select>
    </label><br><br>

    <label>Materie:
        <select name="materie_id" required>
            <option value="">-- Selecteaza materie --</option>
            <?php foreach ($materii as $m): ?>
                <option value="<?= $m['id'] ?>"><?= htmlspecialchars($m['denumire']) ?></option>
            <?php endforeach; ?>
        </select>
    </label><br><br>

    <label>Nota (1 - 10):
        <input type="number" name="nota" step="0.01" min="1" max="10" required>
    </label><br><br>

    <button type="submit">Salveaza nota</button>
</form>

</body>
</html>
