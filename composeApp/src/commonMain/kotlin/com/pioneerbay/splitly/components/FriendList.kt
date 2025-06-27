package com.pioneerbay.splitly.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.fetchFriends
import kotlinx.coroutines.delay

@Composable
fun FriendList(onClick: (Profile) -> Unit = {}) {
    var friends by remember { mutableStateOf<List<Profile>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchFriends(
            onSuccess = { friendsList ->
                friends = friendsList
                isLoading = false
            },
            onError = { errorMessage ->
                error = errorMessage
                isLoading = false
            },
        )
    }

    Box(
        Modifier
            .fillMaxWidth(),
        Alignment.Center,
    ) {
        when {
            isLoading -> CircularProgressIndicator()

            error != null ->
                Text(
                    "Error: $error",
                    color = colorScheme.error,
                    textAlign = TextAlign.Center,
                )

            friends.isEmpty() ->
                Text(
                    "You don't have any friends yet",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )

            else ->
                Column(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    friends.forEachIndexed { index, friend ->
                        AnimatedFriendItem(
                            friend = friend,
                            index = index,
                            onClick = { onClick(friend) },
                        )
                    }
                }
        }
    }
}

@Composable
private fun AnimatedFriendItem(
    friend: Profile,
    index: Int,
    onClick: () -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec =
            spring(
                dampingRatio = 0.6f,
                stiffness = 300f,
            ),
        label = "scale_animation",
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec =
            spring(
                dampingRatio = 0.8f,
                stiffness = 400f,
            ),
        label = "alpha_animation",
    )

    LaunchedEffect(friend) {
        delay(index * 100L) // Stagger the animations
        isVisible = true
    }

    FriendItem(
        friend = friend,
        onClick = onClick,
        scale = scale,
        alpha = alpha,
    )
}
