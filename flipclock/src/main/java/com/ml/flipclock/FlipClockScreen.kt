package com.ml.flipclock

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ml.flipclock.ui.theme.DarkTertiary
import com.ml.flipclock.vm.MainViewModel
import com.ml.flipclock.vm.TimeStatus


/**
 * 竖屏
 */
@Composable
fun FlipClockScreen(viewModel: MainViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val timeStatue by viewModel.timeStatusFlow.collectAsStateWithLifecycle(TimeStatus.new())
        LaunchedEffect(timeStatue) {
            viewModel.loop()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            TimeValue(timeStatue.hour)
            TimeValue(timeStatue.minute)
            TimeValue(timeStatue.second)
//            androidx.compose.foundation.Canvas(
//                modifier = Modifier
//                    .weight(1f, true)
//                    .padding(10.dp)
//                    .aspectRatio(1f, true)
//            ) {
//                //drawText(text = timeStatue.second)
//            }
        }
        Text(
            timeStatue.dateStr,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(30.dp)
        )
        Text(
            timeStatue.dayOfWeek,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
fun RowScope.TimeValue(value: Int, randomColor: Boolean = false) {
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
                    (slideInVertically() { height -> height }).togetherWith(
                        slideOutVertically { height -> -height })
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    (slideInVertically { height -> -height }).togetherWith(
                        slideOutVertically { height -> height })

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

    }
}