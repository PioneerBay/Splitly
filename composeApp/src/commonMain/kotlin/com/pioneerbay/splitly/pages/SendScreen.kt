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
import com.pioneerbay.splitly.components.FriendList
import com.pioneerbay.splitly.components.Icon
import com.pioneerbay.splitly.components.NavBarPage
import org.jetbrains.compose.resources.painterResource
import splitly.composeapp.generated.resources.Res.drawable
import splitly.composeapp.generated.resources.send

@Composable
fun SendScreen() =
    NavBarPage {
//        Icon(
//            painterResource(drawable.upload),
//            "Send",
//            Modifier
//                .align(Alignment.TopStart)
//                .padding(36.dp, 12.dp),
//            tint = colorScheme.onBackground,
//        )

        Column(modifier = Modifier.padding(20.dp, top = 40.dp, 20.dp, 20.dp).fillMaxSize(), horizontalAlignment = Alignment.Start) {
            Text("Who do you wanna send money to?", style = typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            FriendList()
        }
    }
