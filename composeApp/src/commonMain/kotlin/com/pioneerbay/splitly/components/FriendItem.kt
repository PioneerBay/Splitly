package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.utils.Profile

@Composable
fun FriendItem(friend: Profile) =
    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shapes.medium,
        cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface,
        ),
        CardDefaults.cardElevation(4.dp),
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
                Text(
                    friend.bio ?: "No bio available",
                    style = typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                )
            }
        }
    }
