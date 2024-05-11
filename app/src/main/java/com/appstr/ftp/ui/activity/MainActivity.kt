package com.appstr.ftp.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.appstr.ftp.ui.feed.FeedContainer
import com.appstr.ftp.ui.theme.FTPTheme
import com.appstr.ftp.ui.theme.defaultPalette

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
                    FeedContainer()
                }
            }
        }
    }
}
