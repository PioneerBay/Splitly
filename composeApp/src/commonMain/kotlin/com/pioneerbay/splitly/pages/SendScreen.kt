package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.components.FriendList
import com.pioneerbay.splitly.components.NavBarPage
import com.pioneerbay.splitly.components.SendSwipe
import com.pioneerbay.splitly.pages.SendStep.*
import com.pioneerbay.splitly.utils.BackHandler
import com.pioneerbay.splitly.utils.Globals
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

enum class SendStep {
    SELECT_FRIEND,
    ENTER_AMOUNT,
    SEND_PROGRESS,
}

@Serializable
data class Transaction(
    val amount: Double,
    val currency: String,
    val to: String,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SendScreen(onHome: () -> Unit) =
    NavBarPage {
        var currentStep by remember { mutableStateOf(SELECT_FRIEND) }
        var selectedFriend by remember { mutableStateOf<Profile?>(null) }
        var amount by remember { mutableStateOf("") }

        BackHandler {
            when (currentStep) {
                SELECT_FRIEND -> onHome()
                ENTER_AMOUNT -> {
                    selectedFriend = null
                    currentStep = SELECT_FRIEND
                }
                SEND_PROGRESS -> {
                    amount = ""
                    currentStep = ENTER_AMOUNT
                }
            }
        }

        Column(
            Modifier
                .padding(20.dp, top = 40.dp, 20.dp, 20.dp)
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally,
        ) {
            when (currentStep) {
                SELECT_FRIEND -> {
                    FriendSelectionStep(
                        onFriendSelected = { friend ->
                            Logger.d { "Selected friend: $friend" }
                            selectedFriend = friend
                            currentStep = ENTER_AMOUNT
                        },
                    )
                }

                ENTER_AMOUNT -> {
                    AmountEntryStep(
                        selectedFriend = selectedFriend,
                        onBackToFriendSelection = { currentStep = SELECT_FRIEND },
                        onSendMoney = { a ->
                            amount = a
                            Logger.d { "Swipe detected - sending $a to ${selectedFriend?.username}" }
                            currentStep = SEND_PROGRESS
                        },
                    )
                }

                SEND_PROGRESS -> SendProgressStep(amount, selectedFriend, onHome)
            }
        }
    }

@Composable
private fun FriendSelectionStep(onFriendSelected: (Profile) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    Text(
        text = "Who do you want to send money to?",
        style = typography.headlineMedium,
        textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(16.dp))

    // Search bar
    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        label = { Text("Search friends") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
    Spacer(Modifier.height(16.dp))

    FriendList(search = searchText, onClick = onFriendSelected)
}

@Composable
private fun AmountEntryStep(
    selectedFriend: Profile?,
    onBackToFriendSelection: () -> Unit,
    onSendMoney: (amount: String) -> Unit,
) {
    Text(
        text =
            buildAnnotatedString {
                append("How much do you want to send to ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(selectedFriend?.username ?: "Unknown")
                }
                append("?")
            },
        style = typography.headlineMedium,
        textAlign = TextAlign.Center,
    )

    var amount by remember { mutableStateOf("") }
    Spacer(Modifier.height(16.dp))

    // Amount input field
    AmountInputField(
        amount = amount,
        onAmountChange = { newAmount ->
            if (isValidAmountInput(newAmount)) {
                amount = newAmount
            }
        },
    )

    Spacer(Modifier.height(8.dp))

    // Back button
    Button(
        onClick = onBackToFriendSelection,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Select a Different Friend")
    }

    Spacer(Modifier.height(8.dp))

    // Send swipe component
    SendSwipe(
        onSend = { onSendMoney(amount) },
        disabled = amount.isEmpty() || amount.toDoubleOrNull() == null || amount.toDoubleOrNull() == 0.0,
    )
}

@Composable
private fun AmountInputField(
    amount: String,
    onAmountChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = amount,
        onValueChange = { newAmount ->
            // Only accept numeric input with up to one decimal point
            if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
                onAmountChange(newAmount)
            }
        },
        label = { Text("Amount") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        prefix = { Text("$") },
        singleLine = true,
    )
}

@Composable
private fun SendProgressStep(
    amount: String,
    selectedFriend: Profile?,
    onHome: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val transaction =
            Transaction(
                amount = amount.toDouble(),
                currency = "USD",
                to = selectedFriend?.user_id ?: "",
            )
        Logger.d { "Inserting transaction: $transaction" }
        supabase.from("transaction").insert(transaction)
        Logger.d { "Successfully sent $amount to ${selectedFriend?.username}" }

        Globals.notifyTransactionUpdate()
        isLoading = false

        delay(5000)
        onHome()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            Text(
                text = "Sending $$amount to ${selectedFriend?.username}",
                style = typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            var countdown by remember { mutableStateOf(3) }
            var showCountdown by remember { mutableStateOf(false) }
            var alpha by remember { mutableStateOf(0f) }
            LaunchedEffect(Unit) {
                delay(1500)
                showCountdown = true
                val fadeSteps = 10
                repeat(fadeSteps) {
                    alpha = (it + 1) / fadeSteps.toFloat()
                    delay(50)
                }
                repeat(3) {
                    delay(1000)
                    countdown--
                }
            }
            Text(
                text =
                    buildAnnotatedString {
                        append("Successfully sent $$amount to ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(selectedFriend?.username ?: "Unknown")
                        }
                        append("!")
                    },
                style = typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            if (showCountdown) {
                androidx.compose.animation.AnimatedVisibility(visible = showCountdown) {
                    Text(
                        text = "Returning to home in $countdown...",
                        style = typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer(alpha = alpha),
                    )
                }
            }
        }
    }
}

private fun isValidAmountInput(input: String): Boolean = input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))
