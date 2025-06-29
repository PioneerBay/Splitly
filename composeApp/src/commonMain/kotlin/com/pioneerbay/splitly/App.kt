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
import com.pioneerbay.splitly.pages.LoginScreen
import com.pioneerbay.splitly.pages.SettingsScreen
import com.pioneerbay.splitly.utils.Globals.currentUser
import com.pioneerbay.splitly.utils.splitlyColorScheme
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.auth.auth
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
        var isLoggedIn by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            supabase.auth.awaitInitialization()
            val session = supabase.auth.currentSessionOrNull()
            isLoggedIn = session != null
            Logger.d("Supabase-Auth") { "App started with session $session & ${session?.user}" }
            session?.user?.let { user -> currentUser = user }
            Logger.d("Supabase-Auth") { "Current user: $currentUser" }
        }

        if (!isLoggedIn) {
            LoginScreen(onLoginSuccess = { isLoggedIn = true })
        } else {
            var currentPage by remember { mutableStateOf(Page.Home) }
            Column(modifier = Modifier.background(colorScheme.background)) {
                Box(Modifier.weight(1f)) {
                    when (currentPage) {
                        Page.Home -> HomeScreen(onNavigateToSettings = { currentPage = Page.Settings })
                        Page.Settings -> SettingsScreen(onNavigateBack = { currentPage = Page.Home })
                    }
                }
            }
            Box(Modifier.fillMaxSize()) {
                NavBar(
                    currentPage,
                ) { currentPage = Page.Home }
            }
        }
    }
}

enum class Page {
    Home,
    Settings,
}
