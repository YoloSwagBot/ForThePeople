package com.appstr.ftp.ui.screen.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.ui.theme.blueGrey_200
import com.appstr.ftp.ui.theme.blueGrey_500
import com.appstr.ftp.viewmodel.MainVM


@Composable
fun ImageScreen(
    modifier: Modifier = Modifier,

    data: RedditJsonChildData?,

    deviceViewSpecs: DeviceViewSpecs,
    mainVM: MainVM = viewModel()
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = blueGrey_200)
    ) {
        ScreenToolbar(
            deviceViewSpecs = deviceViewSpecs,
            title = data?.title ?: "n/a",
            backgroundColor = blueGrey_500
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data?.url)
                .build(),
            contentDescription = "image",
            contentScale = ContentScale.Fit
        )

        BackHandler {
            mainVM.removeScreen()
        }
    }
}

