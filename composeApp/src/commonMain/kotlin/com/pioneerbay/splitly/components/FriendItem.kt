package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.utils.Profile

@Composable
fun FriendItem(
    friend: Profile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    scale: Float,
    alpha: Float,
) = Box(Modifier.scale(0.5f).shadow(10.dp, shapes.).alpha(0.5f)) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .scale(scale),
        shape = shapes.medium,
        contentPadding = PaddingValues(4.dp),
        // elevation = ButtonDefaults.buttonElevation(16.dp, 16.dp, 16.dp, 16.dp, 16.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            Arrangement.spacedBy(16.dp),
            Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(48.dp)
                    .background(
                        linearGradient(listOf(colorScheme.primary, colorScheme.secondary)),
                        CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    friend.username?.firstOrNull()?.uppercase() ?: "?",
                    style = typography.bodyLarge,
                    color = colorScheme.onPrimary,
                )
            }

            Column(
                Modifier.weight(1f),
                Arrangement.Center,
            ) {
                Text(
                    friend.username ?: "Unknown",
                    style = typography.titleMedium,
                    color = colorScheme.onSurface,
                )
//            Text(
//                friend.bio ?: "No bio available",
//                style = typography.bodySmall,
//                color = colorScheme.onSurfaceVariant,
//            )
            }
        }
    }
}
