package com.pioneerbay.splitly

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() =
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 1179.dp / 3, height = 2556.dp / 3),
            title = "Splitly",
        ) {
            App()
        }
    }
