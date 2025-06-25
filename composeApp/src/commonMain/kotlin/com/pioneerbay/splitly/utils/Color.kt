package com.pioneerbay.splitly.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

fun splitlyColorScheme(): ColorScheme =
    lightColorScheme(
        primary = Color(0xFF2969F3),
        secondary = Color(0xFF03DAC6),
        background = Color(0xFFF8F7FC),
        surface = Color.White,
        onSurface = Color.Black,
        surfaceVariant = Color.White,
    )
