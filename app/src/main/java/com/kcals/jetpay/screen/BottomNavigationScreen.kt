package com.kcals.jetpay.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kcals.data.mock.bottomPages

@Composable
fun BottomNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = "Home",
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        composable("Home") { HomeScreen() }
        composable("Finance") { FinanceScreen() }
        composable("Scan") { ScanScreen() }
        composable("History") { HistoryScreen() }
        composable("Mine") { MineScreen() }
    }
}

@Composable
fun BottomBarScreen(navController: NavController = rememberNavController()) {
    val currentPage = navController.currentBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        bottomPages.forEach { screen ->
            NavigationBarItem(
                selected = currentPage == screen.name,
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.name,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { navController.navigate(screen.name) },
                label = { Text(screen.name) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}