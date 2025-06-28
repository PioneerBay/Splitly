package com.pioneerbay.splitly.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.pioneerbay.splitly.utils.Profile
import com.pioneerbay.splitly.utils.supabase
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

enum class Stages {
    EMAIL,
    OTP,
    USERINFO,
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isLoggedIn by remember { mutableStateOf(false) }
    var stage by remember { mutableStateOf(Stages.EMAIL) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Splitly",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 48.dp),
        )
        Text(
            when (stage) {
                Stages.EMAIL -> "Welcome back!"
                Stages.OTP -> "Enter the code sent to your email"
                Stages.USERINFO -> "Choose a username"
            },
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        fun clickEmail() {
            if (email.isBlank()) return
            stage = Stages.OTP
            coroutineScope.launch {
                try {
                    supabase.auth.signInWith(OTP) {
                        this.email = email
                    }
                } catch (e: Exception) {
                    Logger.e("Error: ${e.message}")
                }
            }
        }

        fun clickOTP() {
            if (otp.isBlank()) return
            stage = Stages.USERINFO
            coroutineScope.launch {
                try {
                    val result = supabase.auth.verifyEmailOtp(OtpType.Email.EMAIL, email, otp)
                    Logger.d { "OTP verified: $result" }
                    isLoggedIn = true
                    val user = supabase.auth.currentUserOrNull()
                    if (user == null) return@launch
                    val profile =
                        supabase
                            .from("profiles")
                            .select {
                                filter {
                                    eq("user_id", user.id)
                                }
                            }.decodeSingle<Profile>()
                    Logger.d { "Profile: $profile" }
                    if (profile.username != null && profile.username.isNotBlank()) {
                        username = profile.username
                        onLoginSuccess()
                    }
                } catch (e: Exception) {
                    Logger.e("Error: ${e.message}")
                }
            }
        }

        fun clickUserInfo() {
            if (username.isBlank()) return
            coroutineScope.launch {
                try {
                    val id =
                        supabase.auth.currentUserOrNull()?.id
                    if (id == null) return@launch
                    supabase.from("profiles").update({
                        set("username", username)
                    }) {
                        filter {
                            eq("user_id", id)
                        }
                    }
                    onLoginSuccess()
                } catch (e: Exception) {
                    Logger.e("Error: ${e.message}")
                }
            }
        }

        when (stage) {
            Stages.EMAIL -> EmailInputField(email, { email = it }) { clickEmail() }
            Stages.OTP -> OtpInputField(otp, { otp = it }) { clickOTP() }
            Stages.USERINFO -> UsernameInputField(username, { username = it }) { clickUserInfo() }
        }

        LoginButton(stage, email, otp, username, isLoggedIn) Button@{
            when (stage) {
                Stages.EMAIL -> {
                    clickEmail()
                }

                Stages.OTP -> {
                    clickOTP()
                }

                Stages.USERINFO -> {
                    clickUserInfo()
                }
            }
        }

        Text(
            text = "Enter your email to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun EmailInputField(
    email: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        placeholder = { Text("Enter your email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
                onDone()
            }),
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )
}

@Composable
private fun OtpInputField(
    otp: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = otp,
        onValueChange = onValueChange,
        label = { Text("OTP") },
        placeholder = { Text("Enter the code") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
                onDone()
            }),
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )
}

@Composable
private fun UsernameInputField(
    username: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = username,
        onValueChange = onValueChange,
        label = { Text("Username") },
        placeholder = { Text("John Holland") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions =
            KeyboardActions(onDone = {
                focusManager.clearFocus()
                onDone()
            }),
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )
}

@Composable
private fun LoginButton(
    stage: Stages,
    email: String,
    otp: String,
    username: String,
    isLoggedIn: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        enabled =
            when (stage) {
                Stages.EMAIL -> email.isNotBlank()
                Stages.OTP -> otp.isNotBlank()
                Stages.USERINFO -> username.isNotBlank() && isLoggedIn
            },
    ) {
        Text(
            text = "Continue",
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
