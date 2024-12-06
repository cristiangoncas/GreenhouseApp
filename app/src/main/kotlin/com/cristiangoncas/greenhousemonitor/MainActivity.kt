package com.cristiangoncas.greenhousemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cristiangoncas.greenhousemonitor.ui.navigation.BottomNavigationBar
import com.cristiangoncas.greenhousemonitor.ui.navigation.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                bottomBar = {
                    BottomNavigationBar(navHostController)
                }
            ) { innerPadding ->
                NavigationGraph(navHostController, innerPadding)
            }
        }
    }
}
