package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// Enum to represent the steps in the send money flow
enum class SendStep {
    SELECT_FRIEND,
    ENTER_AMOUNT,
}

@Composable
fun SendScreen() {
    var currentStep by remember { mutableStateOf(SendStep.SELECT_FRIEND) }
    var selectedFriend by remember { mutableStateOf<Profile?>(null) }
    var amount by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    NavBarPage {
        Column(modifier = Modifier.padding(20.dp, top = 40.dp, 20.dp, 20.dp).fillMaxSize(), horizontalAlignment = Alignment.Start) {
            when (currentStep) {
                SendStep.SELECT_FRIEND -> {
                    Text("Who do you wanna send money to?", style = typography.headlineMedium)
                    Spacer(Modifier.height(16.dp))
                    FriendList { friend ->
                        Logger.d { "Selected friend: $friend" }
                        selectedFriend = friend
                        currentStep = SendStep.ENTER_AMOUNT
                    }
                }

                SendStep.ENTER_AMOUNT -> {
                    Text(
                        text =
                            buildAnnotatedString {
                                append("How much do you want to send to ")
                                withStyle(
                                    SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                    ),
                                ) {
                                    append("${selectedFriend?.username}")
                                }
                                append("?")
                            },
                        style = typography.headlineMedium,
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newAmount ->
                            // Only accept numeric input with up to one decimal point
                            if (newAmount.isEmpty() || newAmount.matches(Regex("^\\d*\\.?\\d*$"))) {
                                amount = newAmount
                            }
                        },
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        prefix = { Text("$") },
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Handle the payment logic here
                            coroutineScope.launch {
                                val transaction =
                                    Transaction(
                                        amount = amount.toDoubleOrNull() ?: 0.0,
                                        currency = "USD", // Assuming USD for simplicity
                                        to = selectedFriend?.user_id ?: "",
                                    )
                                val transactions =
                                    supabase
                                        .from("transaction")
                                        .insert(transaction)
                                Logger.d { "Sending $amount to ${selectedFriend?.username}" }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Send Money")
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = {
                            // Go back to friend selection
                            currentStep = SendStep.SELECT_FRIEND
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Select a Different Friend")
                    }
                }
            }
        }
    }
}

@Serializable
class Transaction(
    val amount: Double,
    val currency: String,
    val to: String,
)
