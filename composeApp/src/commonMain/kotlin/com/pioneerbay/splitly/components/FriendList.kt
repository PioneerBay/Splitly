package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.fetchFriends

@Composable
fun FriendList() {
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
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    "Error: $error",
                    color = colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
            friends.isEmpty() -> {
                Text(
                    "You don't have any friends yet",
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(friends) { friend ->
                        FriendItem(friend)
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendItem(friend: Profile) {
    Button(
        onClick = {},
        Modifier
            .fillMaxWidth(),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(0.dp, 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center,
            ) {}
            Text(
                text = friend.username ?: "Username",
                style = typography.titleMedium,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
