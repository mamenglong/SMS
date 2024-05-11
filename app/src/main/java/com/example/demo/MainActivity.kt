package com.example.demo

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import com.example.Mmkv
import com.example.demo.theme.SMSTheme
import com.example.forward.ForwardSmsService
import com.example.sms.HeadlessSmsSendService
import com.example.sms.SmsObserver
import com.example.sms.SmsUi


class MainActivity : ComponentActivity() {
    val smsResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = it.resultCode == Activity.RESULT_OK
            openSmsForward()
            Log.d("MainActivity", "smsResultLauncher result:$result")
        }
    private val viewModel by viewModels<UiViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mmkv.init(this)
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
                android.Manifest.permission.CAMERA,
            ).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.plus(android.Manifest.permission.POST_NOTIFICATIONS)
                }

            }, 11
        )
        startService(Intent(this, HeadlessSmsSendService::class.java))
        SmsObserver.register(this)
       openSmsForward()
    }
    private fun openSmsForward(){
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()){
            ForwardSmsService.start(this)
        }else{
           com.example.forward.NotificationManager.gotoNotificationSetting(this)
        }
    }
}


