package com.appstr.ftp.ui.screen.feed.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.screen.feed.linkFeedItemContentHeight
import com.appstr.ftp.ui.screen.feed.linkFeedItemHeight
import com.appstr.ftp.ui.screen.feed.titleAreaHeight
import com.appstr.ftp.ui.theme.blue_600
import kotlinx.coroutines.Dispatchers


@Composable
fun FeedItemLink(
    data: RedditJsonChild
){

    // Build an ImageRequest with Coil
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }

        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            super.onSuccess(request, result)
        }
    }
    val imageUrl = data.data?.thumbnail ?: ""
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
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(linkFeedItemHeight)
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(titleAreaHeight)
                .padding(8.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .height(linkFeedItemContentHeight),
        ) {
            // Link Thumbnail
            AsyncImage(
                modifier = Modifier
                    .size(linkFeedItemContentHeight)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterEnd),
                model = imageRequest,
                contentDescription = "image",
                contentScale = ContentScale.Crop
            )
            // Link url
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 128.dp)
                    .align(Alignment.Center),
                text = data.data?.url ?: "error reading text",
                fontSize = 16.sp,
                color = blue_600,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                maxLines = 3,
            )
        }
    }
}


