package com.pioneerbay.splitly.utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS doesn't have a system back button like Android
    // This could be extended to handle swipe gestures if needed
} 