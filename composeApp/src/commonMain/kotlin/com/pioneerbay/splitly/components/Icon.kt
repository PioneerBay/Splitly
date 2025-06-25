package com.pioneerbay.splitly.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Icon(
    painter: Painter,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    tint: Color? = null,
    disabled: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val image = @Composable {
        Image(
            painter,
            contentDescription,
            Modifier.size(size),
            colorFilter = tint?.let { tint(it) },
        )
    }

    IconButton(
        onClick = onClick ?: {},
        enabled = !disabled,
        modifier = modifier.size(size + 20.dp),
    ) {
        image()
    }
}
