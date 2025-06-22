package com.pioneerbay.splitly

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import splitly.composeapp.generated.resources.Res
import splitly.composeapp.generated.resources.compose_multiplatform

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
            ),
        shapes =
            shapes.copy(
                medium = RoundedCornerShape(percent = 20),
            ),
    ) {
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
            // Inside your Column, before the Row with Buttons:
            var text by remember { mutableStateOf("") }
            // Create an interaction source to track focus state
            val interactionSource = remember { MutableInteractionSource() }
            // Observe whether the TextField is focused
            val isFocused by interactionSource.collectIsFocusedAsState()
            // Animate the shadow elevation based on focus state
            val shadowElevation by animateDpAsState(
                targetValue = if (isFocused) 15.dp else 0.dp,
                animationSpec =
                    spring(
                        stiffness = 500f,
                        dampingRatio = 0.5f,
                    ),
                label = "shadowAnimation",
            )

            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter something...") },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(shadowElevation, shapes.medium),
                colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = colorScheme.surface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                shape = shapes.medium,
                interactionSource = interactionSource, // Pass the interaction source to the TextField
            )
            // log text when it changes
            LaunchedEffect(text) {
                println("Text changed: $text")
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
}
