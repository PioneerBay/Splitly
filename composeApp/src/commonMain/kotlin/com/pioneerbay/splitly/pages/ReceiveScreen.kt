package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.components.Icon
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.download

@Composable
fun ReceiveScreen(onNavigateBack: () -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        Alignment.Center,
    ) {
        Icon(
            painterResource(drawable.download),
            "Send",
            Modifier
                .align(Alignment.TopStart)
                .padding((36 + 48 + 24).dp, 12.dp),
            48.dp,
            colorScheme.onBackground,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Receive Money", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("This is the Receive page.")
            Spacer(Modifier.height(32.dp))
            Button(onNavigateBack) {
                Text("Back")
            }
        }
    }
}
