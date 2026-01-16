<?php
$host = 'localhost';
$dbname = 'localfood_db';
$username = 'root';
$password = '';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    echo "✅ CONEXÃO OK!";
} catch(PDOException $e) {
    echo "❌ ERRO: " . $e->getMessage();
}
?>
