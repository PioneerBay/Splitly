package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.components.Icon
import com.pioneerbay.splitly.components.NavBarPage
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.upload

@Composable
fun SendScreen(onNavigateBack: () -> Unit) =
    NavBarPage {
        Icon(
            painterResource(drawable.upload),
            "Send",
            Modifier
                .align(Alignment.TopStart)
                .padding(36.dp, 12.dp),
            tint = colorScheme.onBackground,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Send Money", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("This is the Send page.")
            Spacer(Modifier.height(32.dp))
            Button(onNavigateBack) {
                Text("Back")
            }
        }
    }
