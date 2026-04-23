<?php
require 'config.php';

$stmt = $pdo->query("
    SELECT s.nume AS student, m.denumire AS materie, n.nota, p.username AS profesor
    FROM note n
    JOIN studenti s ON n.student_id = s.id
    JOIN materii m ON n.materie_id = m.id
    JOIN profesori p ON n.profesor_id = p.id
    ORDER BY s.nume, m.denumire
");

$note = $stmt->fetchAll(PDO::FETCH_ASSOC);
?>

<!DOCTYPE html>
<html lang="ro">
<head><meta charset="UTF-8"><title>Note studenti</title></head>
<body>
<h1>Note studenti</h1>

<table border="1" cellpadding="5" cellspacing="0">
    <thead>
        <tr>
            <th>Student</th>
            <th>Materie</th>
            <th>Nota</th>
            <th>Profesor</th>
        </tr>
    </thead>
    <tbody>
        <?php if (!$note): ?>
            <tr><td colspan="4">Nicio nota inregistrata.</td></tr>
        <?php else: ?>
            <?php foreach ($note as $n): ?>
                <tr>
                    <td><?= htmlspecialchars($n['student']) ?></td>
                    <td><?= htmlspecialchars($n['materie']) ?></td>
                    <td><?= htmlspecialchars($n['nota']) ?></td>
                    <td><?= htmlspecialchars($n['profesor']) ?></td>
                </tr>
            <?php endforeach; ?>
        <?php endif; ?>
    </tbody>
</table>

</body>
</html>
