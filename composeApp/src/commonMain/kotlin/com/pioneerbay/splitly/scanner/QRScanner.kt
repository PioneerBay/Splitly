package com.pioneerbay.splitly.scanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Data class to represent a QR scan result
data class QRScanResult(
    val data: String,
    val format: String = "QR_CODE"
)

// Interface for QR scanning functionality
expect class QRScannerController {
    fun startScanning(onResult: (QRScanResult) -> Unit, onError: (Exception) -> Unit)
    fun stopScanning()
}

// Composable for QR Scanner UI
@Composable
expect fun QRScannerView(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit,
    onError: (Exception) -> Unit
) 