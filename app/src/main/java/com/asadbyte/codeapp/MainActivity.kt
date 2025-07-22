package com.asadbyte.codeapp

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.net.URLEncoder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    // Using a single-threaded executor for camera analysis
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            CodeAppTheme {
                // Using a map to pass the bitmap to the next screen to avoid URL length limits
                val bitmapStorage = remember { mutableMapOf<String, Bitmap>() }
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "scanner") {
                    composable("scanner") {
                        QRCodeScannerScreen(
                            navController = navController,
                            bitmapStorage = bitmapStorage,
                            cameraExecutor = cameraExecutor
                        )
                    }
                    composable("result/{bitmap_key}/{content}") { backStackEntry ->
                        val bitmapKey = backStackEntry.arguments?.getString("bitmap_key")
                        val content = backStackEntry.arguments?.getString("content")
                        val bitmap = bitmapStorage[bitmapKey]

                        if (bitmap != null && content != null) {
                            ScanResultScreen(bitmap = bitmap, scannedContent = content) {
                                // Clear the stored bitmap and navigate back
                                bitmapStorage.remove(bitmapKey)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QRCodeScannerScreen(
    navController: NavController,
    bitmapStorage: MutableMap<String, Bitmap>,
    cameraExecutor: ExecutorService
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    var isScanningEnabled by remember { mutableStateOf(true) }

    // Request permission if not granted
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (cameraPermissionState.status.isGranted) {
            Text(
                text = "QR Code Scanner",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Camera Preview
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )

            // This effect will re-run if the permission status changes
            LaunchedEffect(cameraPermissionState.status.isGranted) {
                if (cameraPermissionState.status.isGranted) {
                    isScanningEnabled = true // Re-enable scanning when returning to the screen
                    setupCamera(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        executor = cameraExecutor
                    ) { qrCode ->
                        // This callback is invoked when a QR code is detected
                        if (isScanningEnabled) {
                            isScanningEnabled = false // Prevent multiple scans
                            val bitmap = previewView.bitmap
                            if (bitmap != null) {
                                val bitmapKey = "captured_image_${System.currentTimeMillis()}"
                                bitmapStorage[bitmapKey] = bitmap
                                // URL-encode the content to handle special characters
                                val encodedContent = URLEncoder.encode(qrCode, "UTF-8")
                                navController.navigate("result/$bitmapKey/$encodedContent")
                            }
                        }
                    }
                }
            }
        } else {
            // Show a message and a button to grant permission
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Camera permission is required to scan QR codes.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
    }
}


private fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    executor: ExecutorService,
    onQrCodeScanned: (String) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Set up the Preview use case
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        // Set up the ImageAnalysis use case for QR code scanning
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(executor, QRCodeAnalyzer(onQrCodeScanned))
            }

        // Select the back camera
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind everything before rebinding
            cameraProvider.unbindAll()
            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            Log.e("CameraSetup", "Use case binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}

@Composable
fun ScanResultScreen(bitmap: Bitmap, scannedContent: String, onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Scan Result",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Captured QR Code Image",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Scanned Content:", style = MaterialTheme.typography.titleMedium)
        Text(
            // Decode the content back from URL-encoded format
            text = java.net.URLDecoder.decode(scannedContent, "UTF-8"),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onNavigateBack) {
            Text("Scan Again")
        }
    }
}

private class QRCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    private var isScanning = true

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        isScanning = false // Stop scanning after finding one
                        barcodes.firstOrNull()?.rawValue?.let { qrCodeValue ->
                            onQrCodeScanned(qrCodeValue)
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("QRCodeAnalyzer", "Barcode scanning failed", it)
                    isScanning = true // Re-enable scanning on failure
                }
                .addOnCompleteListener { barcodes ->
                    imageProxy.close()
                    // Re-enable scanning for the next frame if nothing was found
                    if (barcodes.result.isEmpty()) {
                        isScanning = true
                    }
                }
        }
    }
}