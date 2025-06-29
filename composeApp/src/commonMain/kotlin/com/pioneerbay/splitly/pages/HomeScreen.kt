package com.pioneerbay.splitly.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.components.FriendList
import com.pioneerbay.splitly.components.Icon
import com.pioneerbay.splitly.components.RecentTransactions
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.settings

@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    Box(
        Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp),
    ) {
        Icon(
            painterResource(drawable.settings),
            "Settings",
            Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(colorScheme.surface, CircleShape),
            tint = colorScheme.onSurface,
        ) { onNavigateToSettings() }

        Column(
            Modifier
                .align(Alignment.Center)
                .padding(32.dp),
        ) {
            Text("Welcome to Splitly!", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            RecentTransactions()
            Spacer(Modifier.height(16.dp))
//            Button(onClick = {
//                coroutineScope.launch {
//                    val cities = supabase.from("friends").select().decodeList<Friend>()
//                    Logger.d { "Friends fetched: $cities" }
//                }
//            }) {
//                Text("Fetch Friends")
//            }
            Text("Friends", style = typography.bodyLarge)
            Spacer(Modifier.height(8.dp))

            FriendList()
        }
    }
}
