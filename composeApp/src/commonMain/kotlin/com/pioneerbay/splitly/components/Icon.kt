package com.pioneerbay.splitly.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Icon(
    painter: Painter,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    buttonPadding: Dp = 8.dp,
    tint: Color? = null,
    disabled: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val buttonSize = size + buttonPadding * 2
    val interactionSource = remember { MutableInteractionSource() }
    val ripple =
        ripple(
            bounded = true,
            radius = buttonSize / 2,
        )

    Box(
        modifier
            .size(buttonSize)
            .background(Color.Transparent, CircleShape)
            .clickable(
                interactionSource,
                ripple,
                !disabled,
            ) { onClick?.invoke() },
        Alignment.Center,
    ) {
        Image(
            painter,
            contentDescription,
            Modifier.size(size),
            colorFilter = tint?.let { ColorFilter.tint(it) },
        )
    }
}
