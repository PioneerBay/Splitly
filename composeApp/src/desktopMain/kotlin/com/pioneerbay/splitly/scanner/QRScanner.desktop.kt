package com.pioneerbay.splitly.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

actual class QRScannerController {
    actual fun startScanning(onResult: (QRScanResult) -> Unit, onError: (Exception) -> Unit) {
        onError(Exception("QR scanning not implemented for Desktop"))
    }
    
    actual fun stopScanning() {
        // No-op for Desktop
    }
}

@Composable
actual fun QRScannerView(
    modifier: Modifier,
    onQRCodeScanned: (String) -> Unit,
    onError: (Exception) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QR scanning is not available on Desktop",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This feature is currently only available on Android",
            style = MaterialTheme.typography.bodyMedium
        )
    }
} 