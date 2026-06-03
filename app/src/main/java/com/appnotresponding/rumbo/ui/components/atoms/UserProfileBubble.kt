package com.appnotresponding.rumbo.ui.components.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun UserProfileBubble(
    user: User,
    bubbleSize: Dp = 48.dp,
    onImageLoaded: (() -> Unit)? = null,
    preloadedBitmap: ImageBitmap? = null
) {
    val context = LocalContext.current

    val imageRequest = remember(user.profilePictureUrl, context) {
        ImageRequest.Builder(context)
            .data(user.profilePictureUrl)
            .allowHardware(false)
            .build()
    }

    var imageLoadFailed by remember(user.profilePictureUrl) { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(bubbleSize)
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center
    ) {
        when {
            preloadedBitmap != null -> {
                Image(
                    bitmap = preloadedBitmap,
                    contentDescription = "Foto de perfil de ${user.name}",
                    modifier = Modifier
                        .size(bubbleSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                LaunchedEffect(preloadedBitmap) {
                    onImageLoaded?.invoke()
                }
            }

            !user.profilePictureUrl.isNullOrEmpty() && !imageLoadFailed -> {
                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Foto de perfil de ${user.name}",
                    modifier = Modifier
                        .size(bubbleSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    onSuccess = { onImageLoaded?.invoke() },
                    onError = { imageLoadFailed = true }
                )
            }

            else -> {
                if (!user.profilePictureUrl.isNullOrEmpty() && imageLoadFailed) {
                    Icon(
                        modifier = Modifier.size(bubbleSize * 0.5f),
                        painter = painterResource(R.drawable.ic_user),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = user.name.firstOrNull()?.uppercase() ?: "U",
                        fontSize = (bubbleSize.value * 0.4f).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileBubblePreview() {
    RumboTheme(darkTheme = true) {
        UserProfileBubble(
            user = sampleUser,
        )
    }
}