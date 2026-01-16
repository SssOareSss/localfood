package com.example.localfood.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape          // ✅ ADICIONA
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush                    // ✅ ADICIONA
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
data class VendedorDetailData(
    val id: String,
    val nome: String,
    val rating: Float,
    val totalReviews: Int,
    val distancia: String,
    val telefone: String,
    val whatsapp: String,
    val produtos: List<String>,
    val horario: String,
    val endereco: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendedorDetailScreen(navController: NavController, vendedorId: String) {
    val vendedor = when (vendedorId) {
        "1" -> VendedorDetailData(
            "1", "José Silva - Horta do Campo", 4.8f, 127, "1.2km",
            "912 345 678", "+351 912 345 679",
            listOf("🍅 Tomates", "🥬 Alface", "🥕 Cenouras"),
            "Seg-Sáb 8h-19h",
            "Rua das Hortas, 4920 Viana"
        )
        "2" -> VendedorDetailData(
            "2", "Maria Santos - Frescos do Mar", 4.5f, 89, "0.8km",
            "963 456 789", "+351 963 456 790",
            listOf("🦐 Camarão", "🐟 Peixe", "🍋 Limões"),
            "Seg-Dom 7h-20h",
            "Cais de Viana, 4900"
        )
        else -> VendedorDetailData(
            vendedorId, "Vendedor Local", 4.7f, 45, "1.5km",
            "912 345 678", "+351 912 345 679",
            listOf("🥔 Batatas", "🍎 Maçãs"),
            "Seg-Sáb 9h-18h",
            "Centro Viana"
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vendedor.nome) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
                .verticalScroll(rememberScrollState())
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1))
                    ) {
                        Text(
                            vendedor.nome.take(1).uppercase(),
                            fontSize = 28.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        vendedor.nome,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${vendedor.rating} (${vendedor.totalReviews})")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(vendedor.distancia)
                    }
                }
            }

            // Botões Contato - SEM weight()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ContactButtonSimple("💬 WhatsApp", vendedor.whatsapp, Color(0xFF25D366))
                ContactButtonSimple("📞 Ligar", vendedor.telefone, Color(0xFF2196F3))
            }

            // Informações
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "ℹ️ Informações",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InfoRow("📅 Horário", vendedor.horario)
                    InfoRow("📍 Endereço", vendedor.endereco)
                    InfoRow("📱 Telefone", vendedor.telefone)
                }
            }

            // Produtos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "🛒 Produtos (${vendedor.produtos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(vendedor.produtos) { produto ->
                            ProdutoTag(produto)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactButtonSimple(text: String, numero: String, bgColor: Color) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(56.dp)
            .clickable { /* Ação */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ProdutoTag(produto: String) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            produto,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium
        )
    }
}
