package com.example.demo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.theme.SMSTheme
import com.example.sms.HeadlessSmsSendService
import com.example.sms.SmsObserver
import com.example.sms.SmsUi


class MainActivity : ComponentActivity() {
    val smsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = it.resultCode == Activity.RESULT_OK
            Log.d("MainActivity","smsResultLauncher result:$result")
        }
    private val viewModel by viewModels<UiViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmsUi(context = this@MainActivity)
                }
            }
        }
        requestPermissions(
            arrayOf(
                android.Manifest.permission.BROADCAST_SMS,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.CAMERA
            ), 11
        )
        startService(Intent(this, HeadlessSmsSendService::class.java))
        SmsObserver.register(this)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SMSTheme {
        Greeting("Android")
    }
}