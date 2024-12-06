package com.cristiangoncas.greenhousemonitor.ui.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.cristiangoncas.greenhousemonitor.ui.theme.GreenhouseMonitorTheme

@Composable
fun Screen(content: @Composable () -> Unit) {
    GreenhouseMonitorTheme {
        Surface(content = content)
    }
}
