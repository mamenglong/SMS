package com.ml.base.util

import androidx.compose.ui.graphics.Color
import java.util.Random

object ColorUtils {
    fun getRandomColor(): Color {
        val random = Random()
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        return Color(red, green, blue)
    }
}
