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
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.components.NavBar
import com.pioneerbay.splitly.pages.HomeScreen
import com.pioneerbay.splitly.pages.SettingsScreen
import com.pioneerbay.splitly.utils.splitlyColorScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme(
        splitlyColorScheme(),
        shapes.copy(
            medium = RoundedCornerShape(percent = 20),
        ),
    ) {
        var currentPage by remember { mutableStateOf(Pages.Home) }
        var top by remember { mutableStateOf(false) }
        Column(modifier = Modifier.background(colorScheme.background)) {
            Box(Modifier.weight(1f)) {
                when (currentPage) {
                    Pages.Home -> HomeScreen(onNavigateToSettings = { currentPage = Pages.Settings })
                    Pages.Settings ->
                        SettingsScreen(onNavigateBack = {
                            currentPage = Pages.Home
                            top = true
                        })
                }
            }
        }
        Box(Modifier.fillMaxSize()) {
            val log = Logger.withTag("NavBar")
            NavBar(
                currentPage,
                { currentPage = it },
                top,
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
