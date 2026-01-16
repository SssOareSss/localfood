package com.example.localfood.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.localfood.data.auth.AuthManager
import com.example.localfood.presentation.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("login", "register")) {
                MyBottomNav(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (authManager.isLoggedIn()) "home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Telas de autenticação
            composable("login") {
                LoginScreen(navController, authManager)
            }
            composable("register") {
                RegisterScreen(navController)  // ✅ SEM authManager
            }

            // Telas principais
            composable("home") {
                HomeScreen(navController)
            }
            composable("vendedores") {
                VendedoresScreen(navController)
            }
            composable("carrinho") {
                CarrinhoScreen(navController)
            }
            composable("perfil") {
                PerfilScreen(navController)  // ✅ SEM authManager
            }

            composable("vendedor_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                VendedorDetailScreen(navController, id)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyBottomNav(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, "Vendedores") },
            label = { Text("Vendedores") },
            selected = currentRoute == "vendedores",
            onClick = { navController.navigate("vendedores") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, "Carrinho") },
            label = { Text("Carrinho") },
            selected = currentRoute == "carrinho",
            onClick = { navController.navigate("carrinho") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, "Perfil") },
            label = { Text("Perfil") },
            selected = currentRoute == "perfil",
            onClick = { navController.navigate("perfil") { launchSingleTop = true } }
        )
    }
}
