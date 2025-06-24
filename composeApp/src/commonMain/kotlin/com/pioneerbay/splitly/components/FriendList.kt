package com.pioneerbay.splitly.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.pages.Friend
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from

@Composable
fun FriendList() {
    var friends by remember { mutableStateOf<List<Friend>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Fetch friends when the component is first composed
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
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    "Error: $error",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }
            friends.isEmpty() -> {
                Text(
                    "You don't have any friends yet",
                    style = MaterialTheme.typography.bodyLarge,
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
private fun FriendItem(friend: Friend) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Color.White),
        colors =
            CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = "Friend ID: ${friend.id}",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "User 1: ${friend.user_1}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "User 2: ${friend.user_2}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Created: ${friend.created_at}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private suspend fun fetchFriends(
    onSuccess: (List<Friend>) -> Unit,
    onError: (String) -> Unit,
) {
    try {
        val friendsList =
            supabase
                .from("friends")
                .select()
                .decodeList<Friend>()
        onSuccess(friendsList)
    } catch (e: Exception) {
        onError(e.message ?: "Unknown error occurred")
    }
}
