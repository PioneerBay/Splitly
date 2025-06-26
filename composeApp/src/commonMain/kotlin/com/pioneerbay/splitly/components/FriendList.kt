package com.pioneerbay.splitly.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.fetchFriends

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
            .fillMaxWidth()
            .padding(16.dp),
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
                LazyColumn(
                    Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(friends) { friend ->
                        FriendItem(friend) { onClick(friend) }
                    }
                }
        }
    }
}
