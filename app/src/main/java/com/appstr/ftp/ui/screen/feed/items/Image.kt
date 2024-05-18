package com.appstr.ftp.ui.screen.feed.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.screen.feed.titleAreaHeight
import kotlinx.coroutines.Dispatchers


@Composable
fun FeedItemImage(
    data: RedditJsonChild
){

    val showLoading = remember { mutableStateOf(true) }

    // Build an ImageRequest with Coil
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
            showLoading.value = false
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
            showLoading.value = false
        }
    }

    val imageUrl = data.data?.url ?: ""
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .listener(listener)
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

    Column(
        modifier = Modifier.fillMaxSize()
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
            overflow = TextOverflow.Ellipsis
        )
        // Image
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = imageRequest,
            contentDescription = "image",
            contentScale = ContentScale.Fit
        )

    }
}



