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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.unit.dp
import kotlin.math.round

fun formatMoney(balance: Double): String {
    val rounded = round(balance * 100) / 100.0
    val intPart = rounded.toInt()
    val decimalPart = ((rounded - intPart) * 100).toInt()
    return "$intPart.${decimalPart.toString().padStart(2, '0')}"
}

@Composable
fun FriendItem(
    username: String = "Username",
    balance: Double? = null,
    onClick: () -> Unit,
) = Button(
    onClick = onClick,
    modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    shape = shapes.medium,
    colors =
        ButtonDefaults.buttonColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface,
        ),
    contentPadding = PaddingValues(0.dp),
    elevation = ButtonDefaults.buttonElevation(16.dp, 16.dp, 16.dp, 16.dp, 16.dp),
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
                username.firstOrNull()?.uppercase() ?: "?",
                style = typography.bodyLarge,
                color = colorScheme.onPrimary,
            )
        }

        Column(
            Modifier.weight(1f),
            Arrangement.Center,
        ) {
            Text(
                username,
                style = typography.titleMedium,
                color = colorScheme.onSurface,
            )
//            Text(
//                friend.bio ?: "No bio available",
//                style = typography.bodySmall,
//                color = colorScheme.onSurfaceVariant,
//            )
        }
        if (balance == null) return@Button
        Text(
            "$${formatMoney(balance)}",
            style = typography.bodyLarge,
            color = colorScheme.onSurfaceVariant,
        )
    }
}
