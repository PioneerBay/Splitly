package com.pioneerbay.splitly.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class QRScanResult(
    val data: String,
    val format: String = "QR_CODE",
)

expect class QRScannerController {
    fun startScanning(
        onResult: (QRScanResult) -> Unit,
        onError: (Exception) -> Unit,
    )

    fun stopScanning()
}

@Composable
expect fun QRScannerView(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit,
    onError: (Exception) -> Unit,
)
