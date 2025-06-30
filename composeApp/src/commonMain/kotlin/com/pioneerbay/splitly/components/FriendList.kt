package com.pioneerbay.splitly.components

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.data.ProfileMoneyBalance
import com.pioneerbay.splitly.utils.Globals
import com.pioneerbay.splitly.utils.Globals.currentUser
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.fetchFriends
import com.pioneerbay.splitly.utils.fetchMoneyBalances

@Composable
fun FriendList(
    search: String = "",
    onClick: (Profile) -> Unit = {},
) {
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
                    friends
                        .filter { profile ->
                            profile.user_id != currentUser!!.id && profile.username?.lowercase()?.contains(search) == true
                        }.forEach { profile ->
                            FriendItem(profile.username ?: "Username") { onClick(profile) }
                        }
                }
        }
    }
}

@Composable
fun MoneyBalanceList() {
    var profileMoneyBalances by remember { mutableStateOf<List<ProfileMoneyBalance>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        fetchMoneyBalances(
            onSuccess = { friendsList ->
                profileMoneyBalances = friendsList
                Logger.d { "Fetched money balances: $profileMoneyBalances" }
                isLoading = false
            },
            onError = { errorMessage ->
                error = errorMessage
                isLoading = false
            },
        )
    }
    val transactionUpdateTrigger by Globals.transactionUpdateTrigger.collectAsState()

    LaunchedEffect(transactionUpdateTrigger) {
        fetchMoneyBalances(
            onSuccess = { friendsList ->
                profileMoneyBalances = friendsList
                Logger.d { "Updated money balances: $profileMoneyBalances" }
                Logger.d { "CurrentUser: $currentUser" }
            },
            onError = { errorMessage ->
                error = errorMessage
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

            profileMoneyBalances.isEmpty() ->
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
                    profileMoneyBalances.filter { p -> p.user_1 == currentUser!!.id || p.user_2 == currentUser!!.id }.forEach { profile ->
                        val isFirst = profile.user_1 == currentUser!!.id
                        FriendItem(
                            (if (isFirst) profile.username_2 else profile.username_1) ?: "Username",
                            (profile.total_sent_from_user_1_to_user_2 - profile.total_sent_from_user_2_to_user_1) * if (isFirst) 1 else -1,
                        ) { }
                    }
                }
        }
    }
}
