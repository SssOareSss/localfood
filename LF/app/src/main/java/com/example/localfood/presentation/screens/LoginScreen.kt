package com.example.localfood.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.localfood.data.auth.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authManager: AuthManager) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("cliente@teste.com") }
    var password by remember { mutableStateOf("password") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Premium
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🍽️", fontSize = 32.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Bem-vindo de volta",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        "Entre com sua conta",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Campos Premium
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÃO LOGIN - URL CORRIGIDA!
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    isLoading = true
                    errorMessage = ""

                    try {
                        // ✅ URL QUE FUNCIONA NO TEU XAMPP!
                        val url = URL("http://10.0.2.2/localfood/auth.php")
                        val connect = url.openConnection() as HttpURLConnection

                        connect.requestMethod = "POST"
                        connect.doOutput = true
                        connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        connect.connectTimeout = 10000
                        connect.readTimeout = 10000

                        // Dados do formulário (igual register.php)
                        val formData = listOf(
                            "email" to email,
                            "password" to password
                        )

                        val params = formData.joinToString("&") {
                            "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}"
                        }

                        connect.outputStream.use { stream ->
                            stream.write(params.toByteArray())
                        }

                        val responseCode = connect.responseCode
                        val response = if (responseCode == 200) {
                            connect.inputStream.bufferedReader().readText()
                        } else {
                            connect.errorStream?.bufferedReader()?.readText() ?: "Erro $responseCode"
                        }

                        withContext(Dispatchers.Main) {
                            isLoading = false
                            println("Login Response: $response") // Logcat

                            // Verifica resposta do teu auth.php
                            if (response.contains("success") || response.contains("Login") || response.contains("true")) {
                                authManager.saveLogin(email)
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                errorMessage = "Email ou senha incorretos"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            isLoading = false
                            errorMessage = "Erro de conexão: ${e.message}"
                            println("Login Error: ${e.message}") // Logcat
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp
                )
            } else {
                Text("Entrar", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Criar nova conta")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
