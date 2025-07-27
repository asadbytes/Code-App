package com.asadbyte.codeapp.ui.scanner

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.asadbyte.codeapp.R
import com.asadbyte.codeapp.domain.QRScanner
import com.asadbyte.codeapp.ui.theme.CodeAppTheme
import com.asadbyte.codeapp.ui.theme.Gray30
import com.asadbyte.codeapp.ui.theme.MyYellow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    onResult: (Long, Bitmap, String) -> Unit,
    scannerViewModel: ScannerViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var isFlashOn by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }
    val qrScanner = remember { QRScanner(context) }

    // This launcher now correctly loads the bitmap and calls the ViewModel
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                coroutineScope.launch {
                    val code = qrScanner.scanImageFromUri(uri)
                    if (code != null) {
                        // 1. Load the actual bitmap from the URI
                        val bitmap = if (Build.VERSION.SDK_INT < 28) {
                            @Suppress("DEPRECATION")
                            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        } else {
                            val source = ImageDecoder.createSource(context.contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                        // 2. Call the ViewModel, which handles saving and state updates
                        scannerViewModel.onQrCodeScanned(code, bitmap)
                    }
                }
            }
        }
    )

    // This existing logic now works for both camera and gallery scans
    val uiState by scannerViewModel.uiState.collectAsState()
    LaunchedEffect(uiState.scannedCode) {
        if (!uiState.scannedCode.isNullOrEmpty() && uiState.capturedBitmap != null) {
            onResult(uiState.scannedId!!, uiState.capturedBitmap!!, uiState.scannedCode!!)
            scannerViewModel.resetState()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

            // ✨ NEW: Add the scanner overlay shape
            ScannerOverlay(modifier = Modifier.fillMaxSize())

            // Control buttons overlay
            Surface(
                modifier = Modifier
                    .size(500.dp, 100.dp)
                    .padding(vertical = 16.dp, horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(BorderStroke(2.dp, MyYellow), RoundedCornerShape(24.dp))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gray30)
                        .padding(16.dp)
                        .align(Alignment.TopCenter)
                ) {
                    IconButton(
                        onClick = { imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(painterResource(R.drawable.ic_imagesmode), contentDescription = "Scan from Gallery", tint = Color.White)
                    }
                    IconButton(
                        onClick = {
                            isFlashOn = !isFlashOn
                            qrScanner.toggleFlash(isFlashOn)
                        },
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        val icon = if (isFlashOn) R.drawable.ic_flash_on else R.drawable.ic_flash_off
                        Icon(painterResource(icon), contentDescription = "Toggle Flashlight", tint = Color.White)
                    }
                    IconButton(
                        onClick = {
                            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                            isFlashOn = false
                            qrScanner.toggleFlash(false)
                        },
                        modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(painterResource(R.drawable.ic_flip_camera), contentDescription = "Switch Camera", tint = Color.White)
                    }
                }
            }

            // This effect handles the live camera feed
            LaunchedEffect(cameraSelector) {
                val code = qrScanner.startScanning(lifecycleOwner, previewView, cameraSelector).first()
                previewView.bitmap?.let { bitmap ->
                    scannerViewModel.onQrCodeScanned(code, bitmap)
                }
            }
        }
    } else {
        // Permission UI remains the same
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Camera permission is required.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Grant Permission")
            }
        }
    }
}
