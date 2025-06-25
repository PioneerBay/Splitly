package com.pioneerbay.splitly.components

import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
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
import androidx.compose.runtime.LaunchedEffect
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
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.Pages
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.download
import splitly.composeapp.generated.resources.home
import splitly.composeapp.generated.resources.upload

@Composable
fun BoxScope.NavBar(
    currentPage: Pages,
    onNavigate: (Pages) -> Unit,
) {
    var top by remember { mutableStateOf(false) }
    var hideUploadIcon by remember { mutableStateOf(false) }
    var hideDownloadIcon by remember { mutableStateOf(false) }
    var hideHomeIcon by remember { mutableStateOf(false) }
    var pendingPage by remember { mutableStateOf<Pages?>(null) }
    var homeBound: Boolean by remember { mutableStateOf(false) }

    val screenHeight = LocalWindowInfo.current.containerSize.height.dp / LocalDensity.current.density
    val navBarHeight = 96.dp
    val offsetY by animateDpAsState(
        if (top) -(screenHeight - navBarHeight - 42.dp) else 0.dp,
        spring(
            DampingRatioLowBouncy,
            StiffnessVeryLow,
        ),
        "NavBarOffset",
    )

    LaunchedEffect(top) {
        if (top) {
            delay(1200)
            onNavigate(pendingPage!!)
            pendingPage = null
            top = false
            when (currentPage) {
                Pages.Home -> {
                    hideDownloadIcon = false
                    hideUploadIcon = false
                }
                Pages.Send -> {
                    hideHomeIcon = false
                    hideDownloadIcon = false
                }
                Pages.Receive -> {
                    hideHomeIcon = false
                    hideUploadIcon = false
                }
                else -> { /* I have absolutely no idea how this would even happen */ }
            }
        }
    }

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
            .padding(36.dp, 12.dp),
        Arrangement.SpaceBetween,
        if (offsetY >= -8.dp) Alignment.CenterVertically else Alignment.Top,
    ) {
        val iconSize = 48
        Fanimate(!hideUploadIcon, onAnimationEnd = {
            homeBound = !homeBound
            Logger.d { "Animation over" }
        }) {
            Icon(
                painterResource(drawable.upload),
                "Send Money",
                size = iconSize.dp,
                tint = colorScheme.onSurface,
                onClick = {
                    top = true
                    hideUploadIcon = true
                    pendingPage = Pages.Send
                },
            )
        }
        Spacer(Modifier.width(if (homeBound) 24.dp + iconSize.dp else 24.dp))
        Fanimate(!hideDownloadIcon) {
            Icon(
                painterResource(drawable.download),
                "Receive Money",
                size = iconSize.dp,
                tint = colorScheme.onSurface,
                onClick = {
                    top = true
                    hideDownloadIcon = true
                    pendingPage = Pages.Receive
                },
            )
        }
        Spacer(Modifier.weight(1f))
        Fanimate(!hideHomeIcon) {
            Icon(
                painterResource(drawable.home),
                "Home",
                size = iconSize.dp,
                tint = if (currentPage == Pages.Home) Color.Gray else colorScheme.onSurface,
                onClick = {
                    top = true
                    hideHomeIcon = true
                    pendingPage = Pages.Home
                },
                disabled = currentPage == Pages.Home,
            )
        }
    }
}
