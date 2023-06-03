package com.example.demo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun createButton(text: String, block: () -> Unit) {
    Button(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        onClick = {
            block.invoke()
        }) {
        Text(text = text)
    }
}