package com.appstr.ftp.ui.screen.feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.data.RedditJsonChild
import com.appstr.ftp.ui.screen.content.Screen
import com.appstr.ftp.ui.screen.feed.items.FeedItemImage
import com.appstr.ftp.ui.screen.feed.items.FeedItemLink
import com.appstr.ftp.ui.screen.feed.items.FeedItemText
import com.appstr.ftp.ui.screen.feed.items.FeedItemYoutubeVideo
import com.appstr.ftp.ui.theme.blueGrey_100
import com.appstr.ftp.ui.theme.blueGrey_50
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
            .background(color = blueGrey_50)
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

val titleAreaHeight = 64.dp
val defaultFeedItemHeight = 256.dp
val linkFeedItemHeight = 160.dp
val linkFeedItemContentHeight = 128.dp
val feedItemContentHeight = 196.dp

@Composable
fun Feed(
    parentWidth: Dp,
    parentHeight: Dp,
    mainVM: MainVM = viewModel()
){
    val dataset by mainVM.dataset.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == dataset.size
        }
    }
    // load more if scrolled to bottom
    LaunchedEffect(reachedBottom) {
//        Log.d("Carson", "LoadingMore?     reachedBottom: $reachedBottom")
        if (reachedBottom){
            mainVM.loadMore()
        }
    }

    LazyColumn(
        modifier = Modifier
            .width(parentWidth)
            .height(parentHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        items(count = dataset.size+1){ idx ->
            if (dataset.isNotEmpty()){
                if (idx == dataset.size){
                    LoadMoreItem()
                }else{
                    FeedItem(idx, dataset[idx])
                }
            }
        }
    }
}

@Composable
fun FeedItem(
    itemPosition: Int,
    data: RedditJsonChild,
    mainVM: MainVM = viewModel()
){
    val topPadding = if (itemPosition == 0) 64.dp else 8.dp
    Box(
        modifier = Modifier
            .padding(top = topPadding, bottom = 8.dp)
            .fillMaxWidth()
            .height(if (data.getItemType() == FeedItemType.LINK) linkFeedItemHeight else defaultFeedItemHeight)
            .background(color = blueGrey_100)
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
                        FeedItemType.HOSTED_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        FeedItemType.YOUTUBE_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        FeedItemType.OTHER_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        FeedItemType.GIF -> mainVM.addScreen(Screen.ImageScreen(data.data))
                        FeedItemType.IMAGE -> mainVM.addScreen(Screen.ImageScreen(data.data))
                        FeedItemType.TEXT -> mainVM.addScreen(Screen.TextScreen(data.data))
                        FeedItemType.LINK -> mainVM.addScreen(Screen.WebpageScreen(data.data))
                        else -> mainVM.addScreen(Screen.TextScreen(data.data))
                    }
                }
            ),
    ){
        when (data.getItemType()) {
            FeedItemType.HOSTED_VIDEO -> FeedItemText(data = data)
            FeedItemType.YOUTUBE_VIDEO -> FeedItemYoutubeVideo(data = data)
            FeedItemType.OTHER_VIDEO -> FeedItemText(data = data)
            FeedItemType.GIF -> FeedItemImage(data = data)
            FeedItemType.IMAGE -> FeedItemImage(data = data)
            FeedItemType.LINK -> FeedItemLink(data = data)
            FeedItemType.TEXT -> FeedItemText(data = data)
        }
    }
}

@Composable
fun LoadMoreItem(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(128.dp)
                .padding(32.dp)
                .align(Alignment.Center)
        )
    }
}

// =================================================================================================================

enum class FeedItemType {
    HOSTED_VIDEO,
    YOUTUBE_VIDEO,
    OTHER_VIDEO,
    GIF,
    IMAGE,
    TEXT,
    LINK
}

fun RedditJsonChild.getItemType(): FeedItemType = when (this.data?.postHint){
    "image" -> FeedItemType.IMAGE
    "rich:video" -> FeedItemType.YOUTUBE_VIDEO
    "self" -> FeedItemType.TEXT
    "link" -> FeedItemType.LINK
    else -> FeedItemType.LINK
}

// =================================================================================================================

