<?php
header('Content-Type: application/json');

function checkWinner($board) {
    $lines = [
        [0,1,2],[3,4,5],[6,7,8],
        [0,3,6],[1,4,7],[2,5,8],
        [0,4,8],[2,4,6]
    ];
    foreach($lines as $line) {
        if($board[$line[0]] !== null &&
           $board[$line[0]] === $board[$line[1]] &&
           $board[$line[1]] === $board[$line[2]]) {
            return $board[$line[0]];
        }
    }
    if(!in_array(null, $board, true)) {
        return 'draw';
    }
    return null;
}

$request_body = file_get_contents('php://input');
$data = json_decode($request_body, true);

if(!isset($data['board']) || !is_array($data['board']) || count($data['board']) !== 9) {
    echo json_encode(['error' => 'Tabla invalida']);
    exit;
}

$board = $data['board'];

//null
for($i=0;$i<9;$i++) {
    if(!isset($board[$i]) || ($board[$i] !== 'X' && $board[$i] !== '0')) {
        $board[$i] = null;
    }
}

$computerChar = '0';
$playerChar = 'X';

//mutare
for($i=0;$i<9;$i++) {
    if($board[$i] === null) {
        $board[$i] = $computerChar;
        break;
    }
}

$winner = checkWinner($board);

echo json_encode([
    'board' => $board,
    'winner' => $winner
]);
