package com.appstr.ftp.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val topPadding = if (itemPosition == 0) 128.dp else 32.dp
    val startEndPadding = 24.dp
    Box(
        modifier = Modifier
            .padding(top = topPadding, bottom = 32.dp)
            .width((feedWidth - (startEndPadding * 2)).coerceAtMost(512.dp))
            .height(192.dp)
            .background(color = blueGrey_200)
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = white,
                    bounded = true
                ),
                onClick = {
                    mainVM.refresh()
                }
            ),
    ){
        Text(text = data.toString())
    }
}

@Composable
fun FeedItemText(){

}

@Composable
fun FeedItemImage(){

}

@Composable
fun FeedItemVideo(){

}
