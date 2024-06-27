package com.ml.flipclock

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ml.flipclock.ui.theme.DarkTertiary
import com.ml.flipclock.ui.theme.FlipClockTheme
import com.ml.flipclock.vm.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(1), SystemBarStyle.auto(1, 1))
        setContent {
            FlipClockTheme {
                MainScreen()
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.screenState.value = newConfig.orientation
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlipClockTheme {
        MainScreen()
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    Column(modifier = Modifier.background(Color.Black)) {
        Spacer(Modifier.statusBarsPadding())
        Box() {
            PortraitScreen()
        }

    }

}

/**
 * 竖屏
 */
@Composable
fun PortraitScreen(viewModel: MainViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val hour by remember { viewModel.hour }
        val minute by remember { viewModel.minute }
        val second by remember { viewModel.second }
        val dateStr by remember { viewModel.dateStr }
        val dayOfWeek by remember { viewModel.dayOfWeek }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            TimeValue(hour,true)
            TimeValue(minute,true)
            TimeValue(second)
        }
        Text(
            dateStr,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(30.dp)
        )
        Text(
            dayOfWeek,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RowScope.TimeValue(value: Int,randomColor:Boolean=false) {
    Box(
        modifier = Modifier
            .weight(1f, true)
            .padding(10.dp)
            .aspectRatio(1f, true)
            .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(CornerSize(10.dp)))
            .background(DarkTertiary)
    ) {
        AnimatedContent(
            modifier = Modifier.align(Alignment.Center),
            targetState = value,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut())
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> height } + fadeOut())

                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = true)
                )
            }) { targetState ->

            Text(
                text = "%02d".format(targetState),
                color = if (randomColor) com.ml.base.util.ColorUtils.getRandomColor() else Color.White,
                fontWeight = FontWeight.W800,
                fontFamily = FontFamily.Monospace,
                fontSize = 160.sp,
            )
        }
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()){

        }
    }
}