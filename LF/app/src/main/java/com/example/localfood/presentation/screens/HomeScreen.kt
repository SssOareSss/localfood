package com.example.localfood.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ProdutoData(
    val nome: String,
    val descricao: String,
    val preco: Float,
    val vendedor: String,
    val distancia: String,
    val isPromocao: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val produtos = listOf(
        ProdutoData("🍅 Tomates Frescos", "Orgânicos da horta local", 2.5f, "José Silva", "1.2km", true),
        ProdutoData("🥕 Cenouras", "Colheita do dia", 1.8f, "Maria Santos", "0.8km"),
        ProdutoData("🥬 Alface", "Hidropónica fresca", 1.2f, "José Silva", "1.2km"),
        ProdutoData("🍓 Morangos", "Promoção especial!", 4.5f, "António Costa", "2.1km", true)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "LocalFood",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Localização
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Viana do Castelo", fontWeight = FontWeight.Bold)
                        Text("32 produtos próximos", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Cards - SEM weight()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("🥬 Frescos", "24")
                StatCard("🔥 Promoções", "8")
                StatCard("⭐ Novos", "5")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "🍀 Produtos Próximos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista Produtos - SEM weight()
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(produtos) { produto ->
                    ProdutoCard(produto) {
                        navController.navigate("vendedores")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(titulo, fontWeight = FontWeight.Medium)
            Text(valor, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProdutoCard(produto: ProdutoData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            ) {
                Text(
                    produto.nome.take(1),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Conteúdo - SEM weight()
            Column(modifier = Modifier.padding(end = 12.dp)) {
                Text(
                    produto.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    produto.descricao,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "${produto.preco}€/kg",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(produto.vendedor, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(produto.distancia, style = MaterialTheme.typography.bodySmall)
                }

                if (produto.isPromocao) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "PROMOÇÃO!",
                        color = Color(0xFFFF5722),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
