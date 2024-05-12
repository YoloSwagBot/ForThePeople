package com.appstr.ftp.ui.screen.feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.activity.Screen
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.theme.blueGrey_100
import com.appstr.ftp.ui.theme.blueGrey_200
import com.appstr.ftp.ui.theme.defaultPalette
import com.appstr.ftp.ui.theme.white
import com.appstr.ftp.viewmodel.MainVM


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedContainer(

    mainVM: MainVM = viewModel()
){

    val isRefreshing by mainVM.isRefreshing.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { mainVM.refresh() },
        refreshingOffset = 96.dp,
        refreshThreshold = PullRefreshDefaults.RefreshThreshold
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(color = blueGrey_100)
    ){
        Feed(maxWidth, maxHeight)
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
            contentColor = defaultPalette[5],
        )
    }
}

val feedItemHeight = 256.dp
val feedItemContentHeight = 196.dp

@Composable
fun Feed(
    parentWidth: Dp,
    parentHeight: Dp,

    mainVM: MainVM = viewModel()
){
    val dataset by mainVM.dataset.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier
            .width(parentWidth)
            .height(parentHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(dataset){ p, d ->
            FeedItem(p, parentWidth, d)
        }
    }
}

@Composable
fun FeedItem(
    itemPosition: Int,
    feedWidth: Dp,

    data: RedditJsonChild,

    mainVM: MainVM = viewModel()
){
    val topPadding = if (itemPosition == 0) 128.dp else 8.dp
    Box(
        modifier = Modifier
            .padding(top = topPadding, bottom = 32.dp)
            .fillMaxWidth()
            .height(feedItemHeight)
            .background(color = blueGrey_200)
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = white,
                    bounded = true
                ),
                onClick = {
                    Log.d("Carson", "onClick item ")
                    when (data.getItemType()) {
                        POST_TYPE.HOSTED_VIDEO -> mainVM.addScreen(Screen.VIDEO_SCREEN)
                        POST_TYPE.YOUTUBE_VIDEO -> mainVM.addScreen(Screen.VIDEO_SCREEN)
                        POST_TYPE.OTHER_VIDEO -> mainVM.addScreen(Screen.VIDEO_SCREEN)
                        POST_TYPE.GIF -> mainVM.addScreen(Screen.IMAGE_SCREEN)
                        POST_TYPE.IMAGE -> mainVM.addScreen(Screen.IMAGE_SCREEN)
                        POST_TYPE.TEXT -> mainVM.addScreen(Screen.TEXT_SCREEN)
                        POST_TYPE.LINK -> mainVM.addScreen(Screen.TEXT_SCREEN)
                        null -> mainVM.addScreen(Screen.TEXT_SCREEN)
                    }
                }
            ),
    ){
        when (data.getItemType()) {
            POST_TYPE.HOSTED_VIDEO -> FeedItemText(data = data)
            POST_TYPE.YOUTUBE_VIDEO -> FeedItemVideo(data = data)
            POST_TYPE.OTHER_VIDEO -> FeedItemText(data = data)
            POST_TYPE.GIF -> FeedItemText(data = data)
            POST_TYPE.IMAGE -> FeedItemText(data = data)
            POST_TYPE.TEXT -> FeedItemText(data = data)
            POST_TYPE.LINK -> FeedItemText(data = data)
            null -> FeedItemText(data = data)
        }
    }
}

@Composable
fun FeedItemText(
    data: RedditJsonChild
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        // Desc/etc
        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            text = data.data?.sefttext ?: "error reading text",
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FeedItemImage(
    data: RedditJsonChild

){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        // Desc/etc

    }
}

@Composable
fun FeedItemVideo(
    data: RedditJsonChild

){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp),
            text = data.data?.title ?: "N/A",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        // Desc/etc

    }
}

// ================================================================================================
enum class POST_TYPE {
    HOSTED_VIDEO,
    YOUTUBE_VIDEO,
    OTHER_VIDEO,
    GIF,
    IMAGE,
    TEXT,
    LINK
}

fun RedditJsonChild.getItemType(): POST_TYPE? = when (this.data?.postHint){
    "image" -> POST_TYPE.IMAGE
    "rich:video" -> POST_TYPE.YOUTUBE_VIDEO
    "self" -> POST_TYPE.TEXT
    else -> null
}
