package com.appstr.ftp.ui.screen.feed.items

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.screen.feed.titleAreaHeight
import com.appstr.ftp.ui.theme.orange_A700
import com.appstr.ftp.ui.theme.transparent
import kotlinx.coroutines.Dispatchers


@Composable
fun FeedItemYoutubeVideo(
    data: RedditJsonChild
){
    Log.d("Carson", "FeedItemYoutubeVideo: ${data.data?.url ?: "unknown"}")


    // Build an ImageRequest with Coil
//    val listener = object : ImageRequest.Listener {
//        override fun onError(request: ImageRequest, result: ErrorResult) {
//            super.onError(request, result)
//
//        }
//
//        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
//            super.onSuccess(request, result)
//
//        }
//    }

    val imageUrl = data.data?.thumbnail ?: ""

    val asyncPainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
//            .listener(
//                object : ImageRequest.Listener {
//                    override fun onError(request: ImageRequest, result: ErrorResult) {
//                        super.onError(request, result)
//
//                    }
//                    override fun onSuccess(request: ImageRequest, result: SuccessResult) {
//                        super.onSuccess(request, result)
//
//                    }
//                }
//            )
            .dispatcher(Dispatchers.IO)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
//        .placeholder(placeholder)
//        .error(placeholder)
//        .fallback(placeholder)
            .crossfade(true)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    )

    var bitmap = remember {
        derivedStateOf {
            (asyncPainter.state).let {
                when (it){
                    is AsyncImagePainter.State.Success -> {
                        it.result.drawable.toBitmap()
                    }
                    else -> null
                }
            }
        }
    }

    LaunchedEffect(asyncPainter.state) {
        Log.d("Carson", "LAUNCHED_EFFECT: asyncPainter.state: ${asyncPainter.state.javaClass.simpleName}")
    }

    val palette = remember {
        derivedStateOf {
            bitmap.value.let {
                when (it){
                    null -> null
                    else -> Palette.from(it).generate()
                }
            }
        }
    }
    val thumbnailBG = remember {
        derivedStateOf {
            (palette.value?.darkVibrantSwatch)?.rgb?.let {
                Color(it)
            } ?: transparent
        }
    }
    Log.d("Carson", "isPalette_Null: ${palette.value == null}   ---   isThumbnailBg_transparent: ${thumbnailBG.value == transparent}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(thumbnailBG.value)
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(titleAreaHeight)
                .padding(4.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Image
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.Center),
                model = asyncPainter.request,
                contentDescription = "image",
                contentScale = ContentScale.Fit
            )
            Icon(
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play Video",
                tint = orange_A700
            )
        }
    }
}

