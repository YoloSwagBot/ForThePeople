package com.appstr.ftp.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appstr.ftp.ui.screen.content.ImageScreen
import com.appstr.ftp.ui.screen.content.TextScreen
import com.appstr.ftp.ui.screen.content.VideoScreen
import com.appstr.ftp.ui.screen.feed.FeedContainer
import com.appstr.ftp.ui.theme.FTPTheme
import com.appstr.ftp.ui.theme.defaultPalette
import com.appstr.ftp.viewmodel.MainVM

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FTPTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = defaultPalette[0]
                ) {
                    val mainVM: MainVM by viewModels()
                    val screens = mainVM.screenStack.collectAsStateWithLifecycle()
                    Log.d("Carson", "MainActivity - Screens - ${screens.value.size}")
                    screens.value.forEach {
                        when (it){
                            Screen.MAIN_SCREEN -> FeedContainer()
                            Screen.TEXT_SCREEN -> TextScreen()
                            Screen.IMAGE_SCREEN -> ImageScreen()
                            Screen.VIDEO_SCREEN -> VideoScreen()
                            else -> {}
                        }
                    }

                    BackHandler(true) {

                    }
                }
            }
        }
    }
}

sealed class Screen() {
    object MAIN_SCREEN: Screen()
    object TEXT_SCREEN: Screen()
    object IMAGE_SCREEN: Screen()
    object VIDEO_SCREEN: Screen()
}
