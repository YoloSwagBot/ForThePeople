package com.appstr.ftp.ui.screen.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.ui.theme.blueGrey_300
import com.appstr.ftp.viewmodel.MainVM


@Composable
fun TextScreen(
    modifier: Modifier = Modifier,

    mainVM: MainVM = viewModel()
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = blueGrey_300)
    ) {


        BackHandler {
            mainVM.removeScreen()
        }
    }
}
