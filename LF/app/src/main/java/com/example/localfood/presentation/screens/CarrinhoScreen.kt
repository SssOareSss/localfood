package com.example.localfood.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.localfood.data.CartManager
import com.example.localfood.data.CarrinhoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(navController: NavController) {
    var carrinhos by remember { mutableStateOf(CartManager.getCarrinhos()) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        carrinhos = CartManager.getCarrinhos()
    }

    val totalItens = carrinhos.values.flatten().sumOf { it.quantidade }
    val totalGeral = carrinhos.values.flatten().sumOf { it.preco * it.quantidade }

    if (totalItens == 0) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Carrinho vazio", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("vendedores") }) {
                Text(text = "Ver Vendedores")
            }
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "🛒 Carrinho ($totalItens)",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            carrinhos.entries.forEach { entry ->
                item {
                    CardVendedorCarrinho(
                        vendedor = entry.key,
                        produtos = entry.value,
                        onAlterarQuantidade = { itemId, novaQtd ->
                            CartManager.alterarQuantidade(entry.key, itemId, novaQtd)
                            carrinhos = CartManager.getCarrinhos()
                        },
                        onConfirmar = {
                            val produtosNomes = entry.value.map { it.nome }
                            CoroutineScope(Dispatchers.IO).launch {
                                enviarEncomendaSimples(
                                    userEmail = "cliente@teste.com",
                                    vendedor = entry.key,
                                    produtos = produtosNomes
                                )
                            }
                            CartManager.limparTudo()
                            carrinhos = CartManager.getCarrinhos()
                            successMessage = "Encomenda enviada!"
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL GERAL:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "€${"%.2f".format(totalGeral)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }

        if (successMessage.isNotEmpty()) {
            LaunchedEffect(successMessage) {
                kotlinx.coroutines.delay(2500)
                successMessage = ""
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Snackbar(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = successMessage)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardVendedorCarrinho(
    vendedor: String,
    produtos: List<CarrinhoItem>,
    onAlterarQuantidade: (Int, Int) -> Unit,
    onConfirmar: () -> Unit
) {
    val totalVendedor = produtos.sumOf { it.preco * it.quantidade }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = vendedor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            produtos.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.nome, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "€${"%.2f".format(item.preco)} c/u", style = MaterialTheme.typography.bodySmall)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onAlterarQuantidade(item.id, item.quantidade - 1) }) {
                            Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "${item.quantidade}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { onAlterarQuantidade(item.id, item.quantidade + 1) }) {
                            Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(text = "€${"%.2f".format(item.preco * item.quantidade)}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = { onAlterarQuantidade(item.id, 0) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remover",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL (${produtos.sumOf { it.quantidade }} itens):",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "€${"%.2f".format(totalVendedor)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onConfirmar, modifier = Modifier.fillMaxWidth()) {
                Text(text = "📦 ENCOMENDAR", fontWeight = FontWeight.Bold)
            }
        }
    }
}

suspend fun enviarEncomendaSimples(userEmail: String, vendedor: String, produtos: List<String>) {
    try {
        val url = URL("http://10.0.2.2/localfood/encomendas.php")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

        val produtosStr = produtos.joinToString(", ")
        val data = "user_email=${URLEncoder.encode(userEmail, "UTF-8")}&vendedor_nome=${URLEncoder.encode(vendedor, "UTF-8")}&produtos=${URLEncoder.encode(produtosStr, "UTF-8")}&total=0"

        conn.outputStream.use { it.write(data.toByteArray()) }
        conn.inputStream.bufferedReader().readText()
    } catch (e: Exception) {
        println("Erro: ${e.message}")
    }
}
