package com.appstr.ftp.ui.screen.content

import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.activity.dpToPx
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.ui.theme.blueGrey_300
import com.appstr.ftp.ui.theme.blueGrey_500
import com.appstr.ftp.viewmodel.MainVM
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


@Composable
fun YoutubeVideoScreen(
    modifier: Modifier = Modifier,

    data: RedditJsonChildData?,

    deviceViewSpecs: DeviceViewSpecs,
    mainVM: MainVM = viewModel()
){
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val viewWidthDp = if(deviceViewSpecs.isPortrait) maxWidth else maxWidth-(deviceViewSpecs.statusBarHeight*3)
            val viewWidthPx = viewWidthDp.dpToPx().toInt()
            val viewHeightDp = if(deviceViewSpecs.isPortrait) (maxHeight - deviceViewSpecs.statusBarHeight - deviceViewSpecs.toolbarHeight) else maxHeight
            val viewHeightPx = viewHeightDp.dpToPx().toInt()
            AndroidView(
                modifier = Modifier
                    .width(viewWidthDp)
                    .height(viewHeightDp)
                    .align(Alignment.Center),
                factory = {
                    YouTubePlayerView(it).also { view ->
                        view.addYouTubePlayerListener(
                            object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    youTubePlayer.loadVideo(data.parseYoutubeVideoId(), 0f)
                                }
                            }
                        )
                        view.layoutParams = ViewGroup.LayoutParams(
                            viewWidthPx, viewHeightPx
                        )
                    }
                }
            )
            BackHandler {
                mainVM.removeScreen()
            }
        }

        BackHandler {
            mainVM.removeScreen()
        }
    }
}

fun RedditJsonChildData?.parseYoutubeVideoId(): String {
    val url = this?.url ?: return ""

    val watchEndIdx = url.indexOf("/watch?v=")
    if (watchEndIdx != -1){
        val watchSize = 9
        var watchIdEnd = url.indexOf("&", startIndex = watchEndIdx)
//        Log.d("Carson", "watchEndIdx: $watchEndIdx -- watchIdEnd: $watchIdEnd")
        if (watchIdEnd <= 0) watchIdEnd = url.lastIndex
        val watchId = url.substring(watchEndIdx+watchSize..watchIdEnd)
//        Log.d("Carson", "calcing watch id:   $watchId")
        return watchId
    }

    var subStrIdx = url.indexOf("youtu.be/")
    var subStrSize = 9
    if (subStrIdx == -1){
        subStrIdx = url.indexOf("youtube.com/")
        subStrSize = 12
    }
    if (subStrIdx == -1){
        return ""
    }
    // find ? index
    var idx = subStrIdx + subStrSize
    while (idx in url.indices){
        if (url[idx] == '?') break
        idx++
    }
//    Log.d("Carson", url.substring(subStrIdx+subStrSize until idx))
    return url.substring(subStrIdx+subStrSize until idx)
}
