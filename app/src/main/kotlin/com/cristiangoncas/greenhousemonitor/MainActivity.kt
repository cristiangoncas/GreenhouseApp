package com.cristiangoncas.greenhousemonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cristiangoncas.greenhousemonitor.ui.navigation.Navigation
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeViewModel
import com.cristiangoncas.greenhousemonitor.ui.screen.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navigation()
        }
    }
}
