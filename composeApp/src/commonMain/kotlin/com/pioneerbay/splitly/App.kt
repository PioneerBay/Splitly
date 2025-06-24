package com.pioneerbay.splitly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.components.NavBar
import com.pioneerbay.splitly.pages.HomeScreen
import com.pioneerbay.splitly.pages.SettingsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme =
            colorScheme.copy(
                primary = Color(0xFF2969F3),
                secondary = colorScheme.secondary,
                background = Color(0xFFF8F7FC),
                surface = Color.White,
                onSurface = Color.Black,
                surfaceVariant = Color.White, // Adding a custom surfaceVariant color for cards
            ),
        shapes =
            shapes.copy(
                medium = RoundedCornerShape(percent = 20),
            ),
    ) {
        var currentPage by remember { mutableStateOf(Pages.Home) }
        var visible by remember { mutableStateOf(true) }
        Column(modifier = Modifier.background(colorScheme.background)) {
            Box(Modifier.weight(1f)) {
                when (currentPage) {
                    Pages.Home -> HomeScreen(onNavigateToSettings = { currentPage = Pages.Settings })
                    Pages.Settings ->
                        SettingsScreen(onNavigateBack = {
                            currentPage = Pages.Home
                            visible = !visible
                        })
                }
            }
        }
        Box(Modifier.fillMaxSize()) {
            val log = Logger.withTag("NavBar")
            NavBar(
                currentPage = currentPage,
                onNavigate = { currentPage = it },
                visible,
                onUpload = { log.d { "Clicked upload" } },
                onDownload = { log.d { "Clicked download" } },
            )
        }
    }
}

enum class Pages {
    Home,
    Settings,
}
