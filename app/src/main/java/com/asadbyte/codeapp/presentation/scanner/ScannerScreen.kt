package com.asadbyte.codeapp.presentation.scanner

import android.Manifest
import android.graphics.Bitmap
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asadbyte.codeapp.domain.QRScanner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    onResult: (Bitmap, String) -> Unit,
    scannerViewModel: ScannerViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val uiState by scannerViewModel.uiState.collectAsState()

    // If we have a result, call the onResult lambda and then reset the state
    LaunchedEffect(uiState.scannedCode) {
        if (!uiState.scannedCode.isNullOrEmpty() && uiState.capturedBitmap != null) {
            onResult(uiState.capturedBitmap!!, uiState.scannedCode!!)
            scannerViewModel.resetState() // Reset for the next scan
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (cameraPermissionState.status.isGranted) {
            Text(
                text = "QR Code Scanner",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            AndroidView(factory = { previewView }, modifier = Modifier.weight(1f))

            // Start scanning when the view is ready
            LaunchedEffect(previewView) {
                val qrScanner = QRScanner(context, lifecycleOwner, previewView)
                val code = qrScanner.startScanning().first()
                previewView.bitmap?.let { bitmap ->
                    scannerViewModel.onQrCodeScanned(code, bitmap)
                }

            }
        } else {
            // Show permission request UI
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
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