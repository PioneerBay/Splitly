package com.pioneerbay.splitly.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pioneerbay.splitly.components.Icon
import com.pioneerbay.splitly.components.NavBarPage
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.receive

@Composable
fun ReceiveScreen() =
    NavBarPage {
        Icon(
            painterResource(drawable.receive),
            "Send",
            Modifier
                .align(Alignment.TopStart)
                .padding((36 + 48 + 24).dp, 12.dp),
            tint = colorScheme.onBackground,
            disabled = true,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Receive Money", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("This is the Receive page.")
        }
    }
