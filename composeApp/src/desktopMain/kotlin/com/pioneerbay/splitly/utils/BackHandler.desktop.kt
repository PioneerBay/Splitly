package com.pioneerbay.splitly.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import java.awt.event.KeyListener
import java.awt.event.KeyEvent as AwtKeyEvent
import java.awt.KeyboardFocusManager

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    DisposableEffect(enabled, onBack) {
        val keyListener = object : KeyListener {
            override fun keyPressed(e: AwtKeyEvent) {
                if (enabled && e.keyCode == AwtKeyEvent.VK_ESCAPE) {
                    onBack()
                }
            }
            override fun keyReleased(e: AwtKeyEvent) {}
            override fun keyTyped(e: AwtKeyEvent) {}
        }
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher { e ->
            if (enabled && e.keyCode == AwtKeyEvent.VK_ESCAPE && e.id == AwtKeyEvent.KEY_PRESSED) {
                onBack()
                true
            } else {
                false
            }
        }
        
        onDispose {
            // Cleanup if needed
        }
    }
} 