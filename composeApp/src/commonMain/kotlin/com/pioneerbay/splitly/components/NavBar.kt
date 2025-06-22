package com.pioneerbay.splitly.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.Pages

@Composable
fun NavBar(
    currentPage: Pages,
    onNavigate: (Pages) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = { onNavigate(Pages.Home) },
            enabled = currentPage != Pages.Home,
        ) {
            Text("Home")
        }
        Button(
            onClick = { onNavigate(Pages.Settings) },
            enabled = currentPage != Pages.Settings,
        ) {
            Text("Settings")
        }
    }
}
