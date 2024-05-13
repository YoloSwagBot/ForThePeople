package com.appstr.ftp.ui.screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.data.RedditJsonChildData
import com.appstr.ftp.ui.theme.blueGrey_900
import com.appstr.ftp.ui.theme.red
import com.appstr.ftp.ui.theme.white
import com.appstr.ftp.viewmodel.MainVM


sealed class Screen(
    val data: RedditJsonChildData? = null,
    val deviceViewSpecs: DeviceViewSpecs = DeviceViewSpecs(32.dp,56.dp)
) {
    class MainScreen: Screen()
    class TextScreen(data: RedditJsonChildData?): Screen(data)
    class ImageScreen(data: RedditJsonChildData?): Screen(data)
    class VideoScreen(data: RedditJsonChildData?): Screen(data)
}

data class DeviceViewSpecs(
    val statusBarHeight: Dp,
    val toolbarHeight: Dp
)

@Composable
fun ScreenToolbar(
    toolbarHeight: Dp,

    title: String,

    backgroundColor: Color,

    mainVM: MainVM = viewModel()
){
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarHeight)
            .background(backgroundColor),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            modifier = Modifier
                .size(56.dp)
                .padding(12.dp)
                .clickable(
                    enabled = true,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        color = white,
                        bounded = true,
                        radius = 28.dp
                    ),
                    onClick = {
                        mainVM.removeScreen()
                    }
                ),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back Button",
            tint = blueGrey_900
        )
        Text(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 64.dp)
                .background(red),
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

