package com.example.localfood.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.localfood.data.CartManager

data class VendedorData(
    val id: String,
    val nome: String,
    val foto: String,
    val rating: Float,
    val distancia: String,
    val produtosCount: Int,
    val produtos: List<Pair<String, Double>>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendedoresScreen(navController: NavController) {
    var totalItens by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        totalItens = CartManager.getCarrinhos().values.flatten().sumOf { it.quantidade }
    }

    val vendedores = listOf(
        VendedorData("1", "José Silva", "", 4.8f, "1.2km", 12, listOf("🍅 Tomates" to 2.50, "🥬 Alface" to 1.20, "🥕 Cenouras" to 1.80)),
        VendedorData("2", "Maria Santos", "", 4.5f, "0.8km", 8, listOf("🦐 Camarão" to 8.50, "🐟 Peixe" to 12.00, "🍋 Limões" to 1.50)),
        VendedorData("3", "António Costa", "", 4.9f, "2.1km", 15, listOf("🍓 Morangos" to 4.20, "🍎 Maçãs" to 1.80, "🍌 Bananas" to 2.10)),
        VendedorData("4", "Manuel Pereira", "", 4.3f, "1.5km", 6, listOf("🥔 Batatas" to 1.10, "🌽 Milho" to 2.30))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏪 Vendedores Locais", fontWeight = FontWeight.Bold) },
                actions = {
                    Row {
                        IconButton(onClick = { navController.navigate("carrinho") }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrinho")
                        }
                        if (totalItens > 0) {
                            Text(
                                text = "$totalItens",
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(vendedores) { vendedor ->
                VendedorCard(vendedor) { produtoNome, preco ->
                    CartManager.adicionarItem(produtoNome, preco, vendedor.nome)
                    totalItens = CartManager.getCarrinhos().values.flatten().sumOf { it.quantidade }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VendedorCard(
    vendedor: VendedorData,
    onAdicionarProduto: (String, Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Color(0xFF4CAF50), Color(0xFF81C784))))
                ) {
                    Text(
                        text = vendedor.nome.take(1).uppercase(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = vendedor.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "${vendedor.rating}⭐ ${vendedor.distancia}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vendedor.produtos.take(3)) { (nome, preco) ->
                    Button(
                        onClick = { onAdicionarProduto(nome, preco) },
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(text = nome)
                    }
                }
            }
        }
    }
}
