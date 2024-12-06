package com.cristiangoncas.greenhousemonitor.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Logs,
        BottomNavItem.Heartbeat
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.contentDescription) },
                label = { Text(item.name) },
                selected = currentDestination == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val contentDescription: String? = null
) {
    data object Home :
        BottomNavItem(
            name = "Home",
            route = "home",
            icon = androidx.compose.material.icons.Icons.Filled.Home,
            contentDescription = "Home"
        )

    data object Logs :
        BottomNavItem(
            name = "Logs",
            route = "logs",
            icon = androidx.compose.material.icons.Icons.Filled.Menu,
            contentDescription = "Logs"
        )

    data object Heartbeat :
        BottomNavItem(
            name = "Heartbeat",
            route = "heartbeat",
            icon = androidx.compose.material.icons.Icons.Filled.Favorite,
            contentDescription = "Heartbeat"
        )
}
