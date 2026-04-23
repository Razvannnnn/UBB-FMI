<?php
session_start();
require 'db.php';

// Validare CSRF
if ($_POST['csrf_token'] !== $_SESSION['csrf']) {
    die("CSRF token invalid.");
}

// Validare input
$from = $_POST['from'] ?? '';
$to = $_POST['to'] ?? '';
$allow_connections = isset($_POST['allow_connections']);

if (!$from || !$to || $from === $to) {
    die("Localitatile sunt invalide.");
}

// Escape
$stmt_direct = $pdo->prepare("
    SELECT * FROM trenuri
    WHERE localitate_plecare = :from AND localitate_sosire = :to
    ORDER BY ora_plecare
");
$stmt_direct->execute(['from' => $from, 'to' => $to]);
$direct_routes = $stmt_direct->fetchAll(PDO::FETCH_ASSOC);


$connected_routes = [];

if ($allow_connections) {
    $stmt_conn = $pdo->prepare("
        SELECT
            t1.nr_tren AS tren1, t1.localitate_plecare AS plecare1, t1.localitate_sosire AS legatura,
            t1.ora_plecare AS plecare_ora1, t1.ora_sosire AS sosire_ora1,
            t2.nr_tren AS tren2, t2.localitate_plecare AS legatura2, t2.localitate_sosire AS sosire2,
            t2.ora_plecare AS plecare_ora2, t2.ora_sosire AS sosire_ora2
        FROM trenuri t1
        JOIN trenuri t2 ON t1.localitate_sosire = t2.localitate_plecare
        WHERE t1.localitate_plecare = :from
          AND t2.localitate_sosire = :to
          AND t1.localitate_sosire != :to
          AND t1.localitate_sosire != :from
          AND t1.ora_sosire < t2.ora_plecare -- tren 1 trebuie sa ajunga inainte ca tren 2 sa plece
        ORDER BY t1.ora_plecare
    ");
    $stmt_conn->execute(['from' => $from, 'to' => $to]);
    $connected_routes = $stmt_conn->fetchAll(PDO::FETCH_ASSOC);
}

$two_leg_routes = [];
if ($allow_connections) {
    $stmt_two_leg = $pdo->prepare("
        SELECT
            t1.nr_tren AS tren1, t1.localitate_plecare AS p1, t1.localitate_sosire AS x,
            t1.ora_plecare AS ora_p1, t1.ora_sosire AS ora_x,

            t2.nr_tren AS tren2, t2.localitate_plecare AS x2, t2.localitate_sosire AS y,
            t2.ora_plecare AS ora_x2, t2.ora_sosire AS ora_y,

            t3.nr_tren AS tren3, t3.localitate_plecare AS y2, t3.localitate_sosire AS p3,
            t3.ora_plecare AS ora_y2, t3.ora_sosire AS ora_b

        FROM trenuri t1
        JOIN trenuri t2 ON t1.localitate_sosire = t2.localitate_plecare
        JOIN trenuri t3 ON t2.localitate_sosire = t3.localitate_plecare
        WHERE t1.localitate_plecare = :from
          AND t3.localitate_sosire = :to
          AND t1.localitate_sosire NOT IN (:from, :to)
          AND t2.localitate_sosire NOT IN (:from, :to, t1.localitate_sosire)
          AND t1.ora_sosire < t2.ora_plecare
          AND t2.ora_sosire < t3.ora_plecare
    ");
    $stmt_two_leg->execute(['from' => $from, 'to' => $to]);
    $two_leg_routes = $stmt_two_leg->fetchAll(PDO::FETCH_ASSOC);
}

?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Rezultate cautare</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        h2 { margin-top: 30px; }
        table { border-collapse: collapse; margin-top: 10px; }
        td, th { padding: 6px 12px; border: 1px solid #ccc; }
    </style>
</head>
<body>
    <h1>Rezultate cautare trenuri din <?= htmlspecialchars($from) ?> în <?= htmlspecialchars($to) ?></h1>

    <h2>Curse directe:</h2>
    <?php if (count($direct_routes) > 0): ?>
        <table>
            <tr><th>Nr Tren</th><th>Tip</th><th>Plecare</th><th>Ora</th><th>Sosire</th><th>Ora</th></tr>
            <?php foreach ($direct_routes as $r): ?>
                <tr>
                    <td><?= $r['nr_tren'] ?></td>
                    <td><?= htmlspecialchars($r['tip_tren']) ?></td>
                    <td><?= htmlspecialchars($r['localitate_plecare']) ?></td>
                    <td><?= $r['ora_plecare'] ?></td>
                    <td><?= htmlspecialchars($r['localitate_sosire']) ?></td>
                    <td><?= $r['ora_sosire'] ?></td>
                </tr>
            <?php endforeach; ?>
        </table>
    <?php else: ?>
        <p>Nicio cursa directa gasita.</p>
    <?php endif; ?>

    <?php if ($allow_connections): ?>
        <h2>Curse cu legatura:</h2>
        <?php if (count($connected_routes) > 0): ?>
            <table>
                <tr><th>Tren 1</th><th>Plecare</th><th>→</th><th>Legatura</th><th>Ora sosire</th>
                    <th>Tren 2</th><th>→</th><th>Sosire</th><th>Ora sosire</th></tr>
                <?php foreach ($connected_routes as $r): ?>
                    <tr>
                        <td><?= $r['tren1'] ?></td>
                        <td><?= htmlspecialchars($r['plecare1']) ?> (<?= $r['plecare_ora1'] ?>)</td>
                        <td>→</td>
                        <td><?= htmlspecialchars($r['legatura']) ?> (<?= $r['sosire_ora1'] ?>)</td>
                        <td></td>
                        <td><?= $r['tren2'] ?></td>
                        <td>→</td>
                        <td><?= htmlspecialchars($r['sosire2']) ?> (<?= $r['sosire_ora2'] ?>)</td>
                        <td></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>Nicio cursa cu legatura gasita.</p>
        <?php endif; ?>
    <?php endif; ?>

    <?php if ($allow_connections): ?>
    <h2>Curse cu 2 legaturi:</h2>
    <?php if (count($two_leg_routes) > 0): ?>
        <table>
            <tr>
                <th>Tren 1</th><th>P1</th><th>→</th><th>X</th>
                <th>Tren 2</th><th>X</th><th>→</th><th>Y</th>
                <th>Tren 3</th><th>Y</th><th>→</th><th>B</th>
            </tr>
            <?php foreach ($two_leg_routes as $r): ?>
                <tr>
                    <td><?= $r['tren1'] ?></td>
                    <td><?= htmlspecialchars($r['p1']) ?> (<?= $r['ora_p1'] ?>)</td>
                    <td>→</td>
                    <td><?= htmlspecialchars($r['x']) ?> (<?= $r['ora_x'] ?>)</td>

                    <td><?= $r['tren2'] ?></td>
                    <td><?= htmlspecialchars($r['x2']) ?> (<?= $r['ora_x2'] ?>)</td>
                    <td>→</td>
                    <td><?= htmlspecialchars($r['y']) ?> (<?= $r['ora_y'] ?>)</td>

                    <td><?= $r['tren3'] ?></td>
                    <td><?= htmlspecialchars($r['y2']) ?> (<?= $r['ora_y2'] ?>)</td>
                    <td>→</td>
                    <td><?= $to ?> (<?= $r['ora_b'] ?>)</td>
                </tr>
            <?php endforeach; ?>
        </table>
    <?php else: ?>
        <p>Nicio cursă cu 2 legături găsită.</p>
    <?php endif; ?>
<?php endif; ?>

</body>
</html>
