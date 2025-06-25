package com.pioneerbay.splitly.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pioneerbay.splitly.Page
import com.pioneerbay.splitly.pages.ReceiveScreen
import com.pioneerbay.splitly.pages.SendScreen
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
    var content by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    val screenHeight = LocalWindowInfo.current.containerSize.height.dp / LocalDensity.current.density
    val navBarHeight = 96.dp
    val offsetY by animateDpAsState(
        if (state is BarState.Top) -(screenHeight - navBarHeight - 0.dp) else 0.dp,
        spring(
            dampingRatio = 1f,
            stiffness = 100f,
        ),
        "NavBarOffset",
    )

    LaunchedEffect(state) {
        hideIcons = state is BarState.Top
        when (state) {
            is BarState.Top -> {
                when (state) {
                    BarState.Top.Send -> content = { SendScreen() }
                    BarState.Top.Receive -> content = { ReceiveScreen() }
                    else -> { /* Legitimately how did you get here? */ }
                }
            }
            BarState.Bottom -> content = null
        }
    }

    Fadimate(
        content != null,
        Modifier.align(TopCenter).zIndex(0f),
        fadeIn(animationSpec = tween(durationMillis = 1000)),
        fadeOut(animationSpec = tween(durationMillis = 1000)),
    ) {
        content?.invoke()
    }

    Box(
        Modifier
            .align(BottomCenter)
            .fillMaxWidth()
            .height(navBarHeight - offsetY)
            .background(colorScheme.surface)
            .clip(RoundedCornerShape(30.dp, 30.dp))
            .zIndex(-1f),
    )

    Row(
        Modifier
            .offset(y = offsetY / 20)
            .align(BottomCenter)
            .fillMaxWidth()
            .height(navBarHeight - offsetY)
            .padding(36.dp, 0.dp, 36.dp, 18.dp),
        SpaceBetween,
        Bottom,
    ) {
        val iconSize = 36.dp
        val iconPadding = 12.dp
        Fadimate(!hideIcons) {
            Icon(
                painterResource(drawable.upload),
                "Send Money",
                Modifier.zIndex(2f),
                size = iconSize,
                buttonPadding = iconPadding,
                tint = colorScheme.onSurface,
            ) {
                state = BarState.Top.Send
            }
        }

        Spacer(Modifier.width(24.dp))

        Fadimate(!hideIcons) {
            Icon(
                painterResource(drawable.download),
                "Receive Money",
                Modifier.zIndex(2f),
                iconSize,
                iconPadding,
                colorScheme.onSurface,
            ) {
                state = BarState.Top.Receive
            }
        }

        Spacer(Modifier.weight(1f))

        val homeDisabled = currentPage == Page.Home && state !is BarState.Top
        Icon(
            painterResource(drawable.home),
            "Home",
            Modifier.zIndex(2f),
            iconSize,
            iconPadding,
            if (homeDisabled) Gray else colorScheme.onSurface,
            homeDisabled,
        ) {
            state = BarState.Bottom
            onHome()
        }
    }
}

sealed class BarState {
    sealed class Top : BarState() {
        object Send : Top()

        object Receive : Top()
    }

    object Bottom : BarState()
}
