package com.pioneerbay.splitly.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService

actual class QRScannerController {
    private var imageAnalysis: ImageAnalysis? = null
    private var cameraExecutor: ExecutorService? = null

    actual fun startScanning(
        onResult: (QRScanResult) -> Unit,
        onError: (Exception) -> Unit,
    ) {
        // Implementation handled in the Composable
    }

    actual fun stopScanning() {
        cameraExecutor?.shutdown()
    }
}

@Composable
actual fun QRScannerView(
    modifier: Modifier,
    onQRCodeScanned: (String) -> Unit,
    onError: (Exception) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCameraPermission = granted
            },
        )

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        CameraPreview(
            modifier = modifier,
            onQRCodeScanned = onQRCodeScanned,
            onError = onError,
            lifecycleOwner = lifecycleOwner,
            context = context,
        )
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Camera permission is required to scan QR codes",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { launcher.launch(Manifest.permission.CAMERA) },
            ) {
                Text("Grant Permission")
            }
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    onQRCodeScanned: (String) -> Unit,
    onError: (Exception) -> Unit,
    lifecycleOwner: LifecycleOwner,
    context: Context,
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var preview by remember { mutableStateOf<Preview?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val executor = ContextCompat.getMainExecutor(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    preview =
                        Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    val imageAnalysis =
                        ImageAnalysis
                            .Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(executor, BarcodeAnalyzer(onQRCodeScanned, onError))
                            }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis,
                        )
                    } catch (e: Exception) {
                        onError(e)
                    }
                }, executor)

                previewView
            },
            modifier = Modifier.fillMaxSize(),
        )

        // Overlay with scanning frame
        ScanningOverlay(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun ScanningOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
        ) {
            // Corner indicators
            Box(modifier = Modifier.fillMaxSize()) {
                // Top-left corner
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .size(24.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .fillMaxSize(),
                    )
                    Box(
                        modifier =
                            Modifier
                                .width(3.dp)
                                .fillMaxHeight(),
                    )
                }

                // Top-right corner
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .size(24.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                    )
                    Box(
                        modifier =
                            Modifier
                                .width(3.dp)
                                .fillMaxHeight()
                                .align(Alignment.TopEnd),
                    )
                }

                // Bottom-left corner
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .size(24.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .align(Alignment.BottomStart),
                    )
                    Box(
                        modifier =
                            Modifier
                                .width(3.dp)
                                .fillMaxHeight(),
                    )
                }

                // Bottom-right corner
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .align(Alignment.BottomEnd),
                    )
                    Box(
                        modifier =
                            Modifier
                                .width(3.dp)
                                .fillMaxHeight()
                                .align(Alignment.BottomEnd),
                    )
                }
            }
        }

        Text(
            text = "Place QR code within the frame",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-50).dp),
        )
    }
}

private class BarcodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit,
    private val onError: (Exception) -> Unit,
) : ImageAnalysis.Analyzer {
    private val scanner =
        BarcodeScanning.getClient(
            BarcodeScannerOptions
                .Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build(),
        )

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner
                .process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        barcode.rawValue?.let { qrCodeValue ->
                            onQRCodeScanned(qrCodeValue)
                            return@addOnSuccessListener
                        }
                    }
                }.addOnFailureListener { exception ->
                    onError(exception)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
