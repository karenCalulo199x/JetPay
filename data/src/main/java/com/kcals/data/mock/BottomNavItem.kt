package com.kcals.data.mock
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(val name: String, val icon: ImageVector)

val bottomPages = listOf(
    BottomNavItem("Home", Icons.Filled.Home),
    BottomNavItem("Finance", Icons.Filled.ShoppingCart),
    BottomNavItem("Scan", Icons.Filled.Search),
    BottomNavItem("History", Icons.Filled.DateRange),
    BottomNavItem("Mine", Icons.Filled.Person)
)
