package com.pioneerbay.splitly.pages

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.components.FriendList
import com.pioneerbay.splitly.components.NavBarPage
import com.pioneerbay.splitly.components.SendSwipe
import com.pioneerbay.splitly.utils.Globals
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.Serializable

// Enum to represent the steps in the send money flow
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

@Composable
fun SendScreen() {
    var currentStep by remember { mutableStateOf(SendStep.SELECT_FRIEND) }
    var selectedFriend by remember { mutableStateOf<Profile?>(null) }
    var amount by remember { mutableStateOf("") }

    NavBarPage {
        Column(
            modifier =
                Modifier
                    .padding(20.dp, top = 40.dp, 20.dp, 20.dp)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {
            when (currentStep) {
                SendStep.SELECT_FRIEND -> {
                    FriendSelectionStep(
                        onFriendSelected = { friend ->
                            Logger.d { "Selected friend: $friend" }
                            selectedFriend = friend
                            currentStep = SendStep.ENTER_AMOUNT
                        },
                    )
                }

                SendStep.ENTER_AMOUNT -> {
                    AmountEntryStep(
                        selectedFriend = selectedFriend,
                        onBackToFriendSelection = { currentStep = SendStep.SELECT_FRIEND },
                        onSendMoney = { a ->
                            amount = a
                            Logger.d { "Swipe detected - sending $a to ${selectedFriend?.username}" }
                            currentStep = SendStep.SEND_PROGRESS
                        },
                    )
                }
                SendStep.SEND_PROGRESS -> SendProgressStep(amount, selectedFriend)
            }
        }
    }
}

@Composable
private fun FriendSelectionStep(onFriendSelected: (Profile) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    Text(
        text = "Who do you wanna send money to?",
        style = typography.headlineMedium,
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
    // Title with friend's name
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

        // Notify global listeners that a transaction was completed
        Globals.notifyTransactionUpdate()

        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            Text(
                text = "Sending $amount USD to ${selectedFriend?.username}",
                style = typography.headlineMedium,
            )
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            Text(
                text = "Successfully sent $amount USD to ${selectedFriend?.username}",
                style = typography.headlineMedium,
            )
        }
    }
}

private fun isValidAmountInput(input: String): Boolean = input.isEmpty() || input.matches(Regex("^\\d*\\.?\\d*$"))
