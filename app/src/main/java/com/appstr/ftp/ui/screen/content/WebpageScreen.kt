package com.appstr.ftp.ui.screen.content

import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.ui.theme.blueGrey_300
import com.appstr.ftp.ui.theme.blueGrey_500
import com.appstr.ftp.viewmodel.MainVM
import kotlinx.coroutines.delay


@Composable
fun WebpageScreen(
    modifier: Modifier = Modifier,

    data: RedditJsonChildData?,

    deviceViewSpecs: DeviceViewSpecs,
    mainVM: MainVM = viewModel()
){
    BoxWithConstraints {
        val maxWidth = maxWidth
        val maxHeight = maxHeight
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = blueGrey_300)
        ) {
            ScreenToolbar(
                deviceViewSpecs = deviceViewSpecs,
                title = data?.title ?: "n/a",
                backgroundColor = blueGrey_500
            )

            val url = remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                delay(1000)
                url.value = data?.url ?: ""
            }

            if (url.value.isNotEmpty()){
                FTPWebView(
                    maxWidth = maxWidth,
                    maxHeight = maxHeight,
                    deviceViewSpecs = deviceViewSpecs,
                    url = url.value
                )
            }

            BackHandler {
                mainVM.removeScreen()
            }
        }
    }
}

@Composable
fun FTPWebView(
    maxWidth: Dp,
    maxHeight: Dp,
    url: String,
    deviceViewSpecs: DeviceViewSpecs
){
    val viewWidthDp = if(deviceViewSpecs.isPortrait) maxWidth else maxWidth-(deviceViewSpecs.statusBarHeight*3)
//    val viewWidthPx = viewWidthDp.dpToPx().toInt()
    val viewHeightDp = if(deviceViewSpecs.isPortrait) (maxHeight - deviceViewSpecs.statusBarHeight - deviceViewSpecs.toolbarHeight) else maxHeight
//    val viewHeightPx = viewHeightDp.dpToPx().toInt()

    AndroidView(
        modifier = Modifier
            .height(viewHeightDp)
            .width(maxWidth),
        factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }, update = {
        it.loadUrl(url)
    })
}