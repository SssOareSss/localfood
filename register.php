<?php
require 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    $input = file_get_contents("php://input");
    $data = json_decode($input, true);
    
    if (json_last_error() !== JSON_ERROR_NONE || !$data) {
        echo json_encode(['success' => false, 'message' => 'JSON inválido']);
        exit();
    }
    
    
    $email = isset($data['email']) ? trim($data['email']) : '';
    $password = isset($data['password']) ? $data['password'] : '';
    $nome = isset($data['nome']) ? trim($data['nome']) : '';
    $tipo = isset($data['tipo']) ? $data['tipo'] : '';
    $telefone = isset($data['telefone']) ? trim($data['telefone']) : '';
    
    if (empty($email) || empty($password) || empty($nome) || empty($tipo)) {
        echo json_encode(['success' => false, 'message' => 'Todos os campos são obrigatórios']);
        exit();
    }
    
    if (!in_array($tipo, ['cliente', 'vendedor'])) {
        echo json_encode(['success' => false, 'message' => 'Tipo inválido']);
        exit();
    }
    
    
    $stmt = $pdo->prepare("SELECT id FROM usuarios WHERE email = ?");
    $stmt->execute([$email]);
    
    if ($stmt->rowCount() > 0) {
        echo json_encode(['success' => false, 'message' => 'Email já cadastrado']);
        exit();
    }
    
    
    $password_hash = password_hash($password, PASSWORD_DEFAULT);
    $stmt = $pdo->prepare("INSERT INTO usuarios (email, password, nome, tipo, telefone) VALUES (?, ?, ?, ?, ?)");
    
    if ($stmt->execute([$email, $password_hash, $nome, $tipo, $telefone])) {
        echo json_encode(['success' => true, 'message' => 'Registrado com sucesso!']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Erro interno do servidor']);
    }
} else {
    echo json_encode(['success' => false, 'message' => 'Método não permitido']);
}
?>
