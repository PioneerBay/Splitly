package com.pioneerbay.splitly.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.Greeting
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res
import splitly.composeapp.generated.resources.compose_multiplatform

@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit) {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier =
            Modifier
                .safeContentPadding()
                .fillMaxSize()
                .background(colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = { showContent = !showContent },
                Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp),
                shape = shapes.medium,
                colors = buttonColors(colorScheme.secondary),
            ) {
                Text("NEWER Click me!", modifier = Modifier.padding(0.dp), maxLines = 1, softWrap = false)
            }
            Button(
                onClick = { showContent = !showContent },
                Modifier.weight(2f),
                shape = RoundedCornerShape(percent = 50),
            ) {
                Text("NEWER Click me!", maxLines = 1, softWrap = false)
            }
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}
