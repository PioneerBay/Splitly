package com.pioneerbay.splitly

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.pioneerbay.splitly.components.NavBar
import com.pioneerbay.splitly.pages.HomeScreen
import com.pioneerbay.splitly.pages.SettingsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentPage by remember { mutableStateOf(Pages.Home) }
        Column {
            NavBar(currentPage = currentPage, onNavigate = { currentPage = it })
            Box(Modifier.fillMaxWidth()) {
                when (currentPage) {
                    Pages.Home -> HomeScreen(onNavigateToSettings = { currentPage = Pages.Settings })
                    Pages.Settings -> SettingsScreen(onNavigateBack = { currentPage = Pages.Home })
                }
            }
        }
    }
}

enum class Pages {
    Home,
    Settings,
}
