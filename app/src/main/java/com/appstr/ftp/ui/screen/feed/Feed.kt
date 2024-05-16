package com.appstr.ftp.ui.screen.feed

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
    val topPadding = if (itemPosition == 0) 64.dp else 8.dp
    Box(
        modifier = Modifier
            .padding(top = topPadding, bottom = 8.dp)
            .fillMaxWidth()
            .height(if (data.getItemType() == POST_TYPE.LINK) linkFeedItemHeight else defaultFeedItemHeight)
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
                        POST_TYPE.HOSTED_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        POST_TYPE.YOUTUBE_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        POST_TYPE.OTHER_VIDEO -> mainVM.addScreen(Screen.VideoScreen(data.data))
                        POST_TYPE.GIF -> mainVM.addScreen(Screen.ImageScreen(data.data))
                        POST_TYPE.IMAGE -> mainVM.addScreen(Screen.ImageScreen(data.data))
                        POST_TYPE.TEXT -> mainVM.addScreen(Screen.TextScreen(data.data))
                        POST_TYPE.LINK -> mainVM.addScreen(Screen.WebpageScreen(data.data))
                        else -> mainVM.addScreen(Screen.TextScreen(data.data))
                    }
                }
            ),
    ){
        when (data.getItemType()) {
            POST_TYPE.HOSTED_VIDEO -> FeedItemText(data = data)
            POST_TYPE.YOUTUBE_VIDEO -> FeedItemYoutubeVideo(data = data)
            POST_TYPE.OTHER_VIDEO -> FeedItemText(data = data)
            POST_TYPE.GIF -> FeedItemImage(data = data)
            POST_TYPE.IMAGE -> FeedItemImage(data = data)
            POST_TYPE.LINK -> FeedItemLink(data = data)
            POST_TYPE.TEXT -> FeedItemText(data = data)
        }
    }
}


// =================================================================================================================

enum class POST_TYPE {
    HOSTED_VIDEO,
    YOUTUBE_VIDEO,
    OTHER_VIDEO,
    GIF,
    IMAGE,
    TEXT,
    LINK
}

fun RedditJsonChild.getItemType(): POST_TYPE = when (this.data?.postHint){
    "image" -> POST_TYPE.IMAGE
    "rich:video" -> POST_TYPE.YOUTUBE_VIDEO
    "self" -> POST_TYPE.TEXT
    "link" -> POST_TYPE.LINK
    else -> POST_TYPE.TEXT
}

// =================================================================================================================

fun Modifier.shimmerLoadingAnimation(
    show: Boolean,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    if (!show) return this
    return composed {

        val shimmerColors = listOf(
            blueGrey_100.copy(alpha = 0.3f),
            blueGrey_100.copy(alpha = 0.5f),
            blueGrey_100.copy(alpha = 1.0f),
            blueGrey_100.copy(alpha = 0.5f),
            blueGrey_100.copy(alpha = 0.3f),
        )

        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
        )
    }
}