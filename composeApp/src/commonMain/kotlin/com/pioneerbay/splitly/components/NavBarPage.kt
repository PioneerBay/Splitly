package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NavBarPage(content: @Composable BoxScope.() -> Unit) =
    Box(
        Modifier.fillMaxSize().background(colorScheme.surface),
        Alignment.Center,
        content = content,
    )
