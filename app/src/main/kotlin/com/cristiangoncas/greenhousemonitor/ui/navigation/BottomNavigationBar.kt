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
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(BottomNavItem.Home.icon, contentDescription = "Home") },
            label = { Text(BottomNavItem.Home.name) },
            selected = currentDestination == BottomNavItem.Home.route,
            onClick = {
                navController.navigate(BottomNavItem.Home.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(BottomNavItem.Logs.icon, contentDescription = "Logs") },
            label = { Text(BottomNavItem.Logs.name) },
            selected = currentDestination == BottomNavItem.Logs.route,
            onClick = {
                navController.navigate(BottomNavItem.Logs.route) {
                    launchSingleTop = true
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(BottomNavItem.Heartbeat.icon, contentDescription = "Heartbeat") },
            label = { Text(BottomNavItem.Heartbeat.name) },
            selected = currentDestination == BottomNavItem.Heartbeat.route,
            onClick = {
                navController.navigate(BottomNavItem.Heartbeat.route) {
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

sealed class BottomNavItem(val name: String, val route: String, val icon: ImageVector) {
    data object Home :
        BottomNavItem(
            "Home",
            "home",
            androidx.compose.material.icons.Icons.Filled.Home
        )

    data object Logs :
        BottomNavItem(
            "Logs",
            "logs",
            androidx.compose.material.icons.Icons.Filled.Menu
        )

    data object Heartbeat :
        BottomNavItem(
            "Heartbeat",
            "heartbeat",
            androidx.compose.material.icons.Icons.Filled.Favorite
        )
}
