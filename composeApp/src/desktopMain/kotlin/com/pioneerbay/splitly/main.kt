@file:Suppress("ktlint:standard:filename")

package com.pioneerbay.splitly

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Toolkit

fun main() =
    application {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        Window(
            onCloseRequest = ::exitApplication,
            title = "Splitly",
            state =
                rememberWindowState(
                    width = (screenSize.height * 1179 / 2556).dp,
                    height = screenSize.height.dp,
                    position = WindowPosition((screenSize.width - (screenSize.height * 1179 / 2556)).dp, 0.dp),
                ),
        ) {
            App()
        }
    }
