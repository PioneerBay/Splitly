package com.pioneerbay.splitly.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.pioneerbay.splitly.utils.Globals.currentUser
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

@Composable
fun QRPrevieew() {
    Image(
        painter =
            rememberQrCodePainter(
                currentUser!!.id,
            ),
        contentDescription = "QR code referring to the example.com website",
    )
}
