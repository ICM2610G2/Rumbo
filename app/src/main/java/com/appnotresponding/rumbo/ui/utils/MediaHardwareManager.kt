package com.appnotresponding.rumbo.ui.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

data class MediaHardwareState(
    val imageUri: Uri?,
    val launchCamera: () -> Unit,
    val launchGallery: () -> Unit,
    val clearImage: () -> Unit
)

@Composable
fun rememberMediaHardwareManager(context: Context = LocalContext.current): MediaHardwareState {
    var currentImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            currentImageUri = tempCameraUri
        }
    }

    val launchGallery: () -> Unit = {
        galleryLauncher.launch("image/*")
    }

    val launchCamera: () -> Unit = {
        val uri = createImageUri(context)
        tempCameraUri = uri
        cameraLauncher.launch(uri)
    }

    val clearImage: () -> Unit = {
        currentImageUri = null
    }

    return MediaHardwareState(
        imageUri = currentImageUri,
        launchCamera = launchCamera,
        launchGallery = launchGallery,
        clearImage = clearImage
    )
}

private fun createImageUri(context: Context): Uri {
    val imageFile = File(context.filesDir, "cameraPic.jpg")
    return FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", imageFile
    )
}
