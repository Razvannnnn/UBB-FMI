<?php
require 'config.php';
require_login();

$errors = [];
$success = '';

// Procesare upload
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!verify_csrf_token($_POST['csrf_token'] ?? '')) {
        $errors[] = "Token CSRF invalid.";
    } elseif (!isset($_FILES['poza']) || $_FILES['poza']['error'] !== UPLOAD_ERR_OK) {
        $errors[] = "Nu a fost selectată o poză validă.";
    } else {
        $file = $_FILES['poza'];

        // Valid securitate - tip si dimensiune
        $allowed_types = ['image/jpeg', 'image/png', 'image/gif'];
        if (!in_array($file['type'], $allowed_types)) {
            $errors[] = "Formatul fisierului nu este permis. Acceptam doar JPEG, PNG, GIF.";
        }

        if ($file['size'] > 5 * 1024 * 1024) {
            $errors[] = "Fisierul este prea mare. Max 5MB.";
        }

        // Valid suplimentara (mime)
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $mime_type = finfo_file($finfo, $file['tmp_name']);
        finfo_close($finfo);
        if (!in_array($mime_type, $allowed_types)) {
            $errors[] = "Formatul fisierului nu este permis (verificare MIME).";
        }

        if (!$errors) {
            // Generare fisier
            $ext = pathinfo($file['name'], PATHINFO_EXTENSION);
            $nume_fisier = bin2hex(random_bytes(16)) . '.' . $ext;

            $dest = __DIR__ . '/uploads/' . $nume_fisier;

            if (!is_dir(__DIR__ . '/uploads')) {
                mkdir(__DIR__ . '/uploads', 0755, true);
            }

            if (move_uploaded_file($file['tmp_name'], $dest)) {
                // Inserare in baza de date
                $stmt = $pdo->prepare("INSERT INTO poze (utilizator_id, nume_fisier) VALUES (?, ?)");
                $stmt->execute([$_SESSION['utilizator_id'], $nume_fisier]);
                $success = "Fisierul a fost încarcat cu succes.";
            } else {
                $errors[] = "Eroare la salvarea fisierului.";
            }
        }
    }
}

$stmt = $pdo->prepare("SELECT * FROM poze WHERE utilizator_id = ? ORDER BY data_upload DESC");
$stmt->execute([$_SESSION['utilizator_id']]);
$poze = $stmt->fetchAll();

$csrf_token = generate_csrf_token();
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Upload poze</title></head>
<body>
<h1>Upload poze</h1>
<p>
  <a href="logout.php">Deconectare</a> | 
  <a href="profil.php?id=<?= $_SESSION['utilizator_id'] ?>">Vezi profilul tau</a> | 
  <a href="lista_utilizatori.php">Vezi alti utilizatori</a>
</p>


<?php if ($success): ?>
    <p style="color:green;"><?= htmlspecialchars($success) ?></p>
<?php endif; ?>

<?php if ($errors): ?>
    <ul style="color:red;">
        <?php foreach ($errors as $e) echo "<li>" . htmlspecialchars($e) . "</li>"; ?>
    </ul>
<?php endif; ?>

<form method="post" enctype="multipart/form-data" novalidate>
    <input type="hidden" name="csrf_token" value="<?= htmlspecialchars($csrf_token) ?>">
    <label>Alege o poza: <input type="file" name="poza" accept=".jpg,.jpeg,.png,.gif" required></label><br><br>
    <button type="submit">Incarca</button>
</form>

<h2>Pozele tale</h2>
<?php if (count($poze) === 0): ?>
    <p>Nu ai încarcat poze.</p>
<?php else: ?>
    <?php foreach ($poze as $p): ?>
        <div style="margin-bottom:10px;">
            <img src="uploads/<?= htmlspecialchars($p['nume_fisier']) ?>" alt="Poză" style="max-width:200px;"><br>
            <form method="post" action="sterge_poze.php" style="display:inline;" onsubmit="return confirm('Sigur vrei să ștergi această poză?');">
                <input type="hidden" name="csrf_token" value="<?= htmlspecialchars($csrf_token) ?>">
                <input type="hidden" name="id_poze" value="<?= (int)$p['id'] ?>">
                <button type="submit">Sterge poza</button>
            </form>
        </div>
    <?php endforeach; ?>
<?php endif; ?>

</body>
</html>
