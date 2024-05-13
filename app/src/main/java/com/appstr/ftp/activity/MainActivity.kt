package com.appstr.ftp.activity

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.appstr.ftp.ui.screen.content.DeviceViewSpecs
import com.appstr.ftp.ui.screen.content.ImageScreen
import com.appstr.ftp.ui.screen.content.Screen
import com.appstr.ftp.ui.screen.content.TextScreen
import com.appstr.ftp.ui.screen.content.VideoScreen
import com.appstr.ftp.ui.screen.feed.FeedContainer
import com.appstr.ftp.ui.theme.FTPTheme
import com.appstr.ftp.ui.theme.defaultPalette
import com.appstr.ftp.viewmodel.MainVM


class MainActivity : ComponentActivity() {

    private var statusBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            FTPTheme {
                MainContainer(statusBarHeight)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        statusBarHeight = getStatusBarHeight()
    }

    private fun Activity.getStatusBarHeight(): Int {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                val windowInsets: WindowInsets = this.window.decorView.getRootWindowInsets()
                return windowInsets.getInsets(WindowInsets.Type.statusBars()).top
            }
            Build.VERSION.SDK_INT >= 28 -> {
                val windowInsets: WindowInsets = this.window.decorView.getRootWindowInsets()
                val displayCutout = windowInsets.displayCutout
                if (displayCutout != null) {
                    return displayCutout.safeInsetTop
                }
            }
        }
        return 24
    }
}

@Composable
fun MainContainer(
    statusBarHeight: Int,
    mainVM: MainVM = viewModel()
){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = defaultPalette[0]
    ) {
        // (1/3) status bar height
        val context = LocalDensity.current
        val statusBarHeightDP = remember {
            derivedStateOf {
                with(context) { statusBarHeight.toDp() }
            }
        }
        // (2/3) toolbar height
        var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
        val configuration = LocalConfiguration.current
        LaunchedEffect(configuration) {
            snapshotFlow { configuration.orientation }
                .collect { orientation = it }
        }
        val actionBarHeight = remember {
            derivedStateOf {
                when (orientation){
                    1 -> 56.dp
                    else -> 64.dp
                }
            }
        }
        // (3/3)
        val deviceViewSpecs = remember {
            derivedStateOf { DeviceViewSpecs(statusBarHeightDP.value, actionBarHeight.value) }
        }

//        Log.d("Carson", "statusbar: ${statusBarHeightDP.value} --- actionBarHeight: ${actionBarHeight.value}")


        val screens = mainVM.screenStack.collectAsStateWithLifecycle()
//        Log.d("Carson", "MainActivity - Screens - ${screens.value.size}")
        screens.value.forEach {
            when (it){
                is Screen.MainScreen -> FeedContainer()
                is Screen.TextScreen -> TextScreen(data = it.data, deviceViewSpecs = deviceViewSpecs.value)
                is Screen.ImageScreen -> ImageScreen(data = it.data, deviceViewSpecs = deviceViewSpecs.value)
                is Screen.VideoScreen -> VideoScreen(data = it.data, deviceViewSpecs = deviceViewSpecs.value)
                else -> {}
            }
        }

        BackHandler(true) {}
    }
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }