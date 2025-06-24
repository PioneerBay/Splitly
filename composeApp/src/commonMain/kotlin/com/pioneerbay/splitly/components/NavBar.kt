package com.pioneerbay.splitly.components

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.Pages
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res
import splitly.composeapp.generated.resources.download
import splitly.composeapp.generated.resources.home
import splitly.composeapp.generated.resources.upload

@Composable
fun BoxScope.NavBar(
    currentPage: Pages,
    onNavigate: (Pages) -> Unit,
    visible: Boolean,
    onUpload: () -> Unit,
    onDownload: () -> Unit,
) {
    val navBarHeight = 96.dp
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else -(navBarHeight + 950.dp),
        label = "NavBarOffset",
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
            .background(color = colorScheme.surface)
            .padding(36.dp, 12.dp),
        Arrangement.SpaceBetween,
        if (visible) Alignment.CenterVertically else Alignment.Top,
    ) {
        val iconSize = 48
        Icon(
            painterResource(Res.drawable.upload),
            "Send Money",
            size = iconSize.dp,
            tint = colorScheme.onSurface,
            onClick = onUpload,
        )
        Spacer(Modifier.width(24.dp))
        Icon(
            painterResource(Res.drawable.download),
            "Receive Money",
            size = iconSize.dp,
            tint = colorScheme.onSurface,
            onClick = onDownload,
        )
        Spacer(Modifier.weight(1f))
        Icon(
            painterResource(Res.drawable.home),
            "Home",
            size = iconSize.dp,
            tint = if (currentPage == Pages.Home) Color.Gray else colorScheme.onSurface,
            onClick = { onNavigate(Pages.Home) },
            disabled = currentPage == Pages.Home,
        )
    }
}
