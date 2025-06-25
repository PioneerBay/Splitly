package com.pioneerbay.splitly.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.Page
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.download
import splitly.composeapp.generated.resources.home
import splitly.composeapp.generated.resources.upload

@Composable
fun BoxScope.NavBar(
    currentPage: Page,
    onHome: () -> Unit,
) {
    var hideIcons by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf<BarState>(BarState.Bottom) }

    val screenHeight = LocalWindowInfo.current.containerSize.height.dp / LocalDensity.current.density
    val navBarHeight = 96.dp
    val offsetY by animateDpAsState(
        if (state is BarState.Top) -(screenHeight - navBarHeight - 0.dp) else 0.dp,
        spring(
            dampingRatio = 1f,
            100f,
        ),
        "NavBarOffset",
    )

    Box(
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(-offsetY + (10.dp.takeIf { offsetY != 0.dp } ?: 0.dp))
            .background(colorScheme.surface),
    ) {}

    Row(
        Modifier
            .offset(y = offsetY / 20)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(navBarHeight - offsetY)
            .clip(RoundedCornerShape(30.dp, 30.dp))
            .background(colorScheme.surface)
            .padding(36.dp, 0.dp, 36.dp, 18.dp),
        Arrangement.SpaceBetween,
        Alignment.Bottom,
    ) {
        val iconSize = 30
        Fanimate(!hideIcons) {
            Icon(
                painterResource(drawable.upload),
                "Send Money",
                size = iconSize.dp,
                tint = colorScheme.onSurface,
                onClick = {
                    state = BarState.Top.Send
                    hideIcons = true
                },
            )
        }
        Spacer(Modifier.width(24.dp))
        Fanimate(!hideIcons) {
            Icon(
                painterResource(drawable.download),
                "Receive Money",
                size = iconSize.dp,
                tint = colorScheme.onSurface,
                onClick = {
                    state = BarState.Top.Receive
                    hideIcons = true
                },
            )
        }

        Spacer(Modifier.weight(1f))

        val homeDisabled = currentPage == Page.Home && state !is BarState.Top
        Icon(
            painterResource(drawable.home),
            "Home",
            size = iconSize.dp,
            tint = if (homeDisabled) Color.Gray else colorScheme.onSurface,
            onClick = {
                state = BarState.Bottom
                hideIcons = false
                onHome()
            },
            disabled = homeDisabled,
        )
    }
}

sealed class BarState {
    sealed class Top : BarState() {
        object Send : Top()

        object Receive : Top()
    }

    object Bottom : BarState()
}
