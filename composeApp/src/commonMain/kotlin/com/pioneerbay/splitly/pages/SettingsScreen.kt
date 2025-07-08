package com.pioneerbay.splitly.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.components.QRPrevieew
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import com.pioneerbay.splitly.scanner.QRScannerView

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    var showScanner by remember { mutableStateOf(false) }
    var qrCodeURL by remember { mutableStateOf<String?>(null) }
    if (showScanner) {
        if (qrCodeURL == null) {
            QRScannerView(
                modifier = Modifier.fillMaxSize(),
                onQRCodeScanned = { scannedData ->
                    qrCodeURL = scannedData
                    Logger.d { "QR Code scanned: $scannedData" }
                },
                onError = { error ->
                    Logger.e { "QR Scanner failed: $error" }
                    showScanner = false
                }
            )
        } else {
            addFriendScreen(qrCodeURL!!)
        }
    } else {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            QRPrevieew()
            Spacer(Modifier.height(8.dp))
            Text("Settings Page (Placeholder)")
            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { showScanner = true }) {
                    Text("Scan QR Code")
                }

                Button(onClick = onNavigateBack) {
                    Text("Back to Home")
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfileCard(profile: Profile) {
    Column(
        modifier =
            Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.8f)
                .shadow(10.dp, shape = RoundedCornerShape(16.dp))
                .background(colorScheme.surface, shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            Modifier
                .size(100.dp)
                .background(
                    linearGradient(listOf(colorScheme.primary, colorScheme.secondary)),
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                profile.username?.firstOrNull()?.uppercase() ?: "?",
                fontSize = 30.sp,
                color = colorScheme.onPrimary,
            )
        }
        Spacer(Modifier.height(38.dp))
        Text("${profile.username}", fontSize = 30.sp)
    }
}

@Composable
fun addFriendScreen(userId: String) {
    var isLoading by remember { mutableStateOf(true) }
    var newFriendProfile by remember { mutableStateOf<Profile?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        try {
            supabase.from("friends").insert(mapOf("user_2" to userId))
            val profile =
                supabase
                    .from("profiles")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }.decodeSingle<Profile>()
            newFriendProfile = profile
            Logger.d { "Added new friend: $profile" }
        } catch (e: Exception) {
            Logger.e { "Error adding friend: $e" }
            errorMessage = "Failed to add friend: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Adding friend...")
            }
            errorMessage != null -> {
                Text("Error: $errorMessage")
            }
            newFriendProfile != null -> {
                Text("Friend added successfully!")
                Spacer(Modifier.height(16.dp))
                ProfileCard(newFriendProfile!!)
            }
        }
    }
}
