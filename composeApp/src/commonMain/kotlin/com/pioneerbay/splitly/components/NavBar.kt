package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.Pages

@Composable
fun BoxScope.NavBar(
    currentPage: Pages,
    onNavigate: (Pages) -> Unit,
) {
    Row(
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height((-80).dp)
            .clip(RoundedCornerShape(30.dp, 30.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(6.dp, 10.dp),
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
