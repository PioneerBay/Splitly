package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SendScreen(onNavigateBack: () -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Send Money", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("This is the Send page.")
            Spacer(Modifier.height(32.dp))
            Button(onClick = onNavigateBack) {
                Text("Back")
            }
        }
    }
}
