package com.appstr.ftp.ui.screen.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.ui.theme.blueGrey_300
import com.appstr.ftp.ui.theme.blueGrey_500
import com.appstr.ftp.viewmodel.MainVM


@Composable
fun WebpageScreen(
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


        BackHandler {
            mainVM.removeScreen()
        }
    }
}