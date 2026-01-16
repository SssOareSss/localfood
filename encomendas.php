<?php
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Content-Type');

include 'config.php';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    
    $user_email = $input['user_email'];
    $vendedor_nome = $input['vendedor_nome'];
    $produtos = json_encode($input['produtos']);
    $total = $input['total'];
    
    
    $stmt = $pdo->prepare("
        INSERT INTO encomendas (user_email, vendedor_nome, produtos, total, data_criacao) 
        VALUES (?, ?, ?, ?, NOW())
    ");
    
    $success = $stmt->execute([$user_email, $vendedor_nome, $produtos, $total]);
    
    echo json_encode([
        "success" => $success,
        "message" => $success ? "✅ Encomenda enviada ao vendedor!" : "❌ Erro ao enviar"
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Método não permitido"]);
}
?>
