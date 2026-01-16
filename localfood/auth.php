<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, GET, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
    exit(0);
}

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    $email = trim($input['email'] ?? '');
    $password = $input['password'] ?? '';
    
    // TEU USER DE TESTE
    if ($email === 'cliente@teste.com' && $password === 'password') {
        echo json_encode([
            'success' => true,
            'message' => 'Login bem-sucedido'
        ]);
        exit;
    }
    
    echo json_encode([
        'success' => false,
        'message' => 'Email ou senha incorretos'
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'Apenas método POST permitido'
    ]);
}
?>
