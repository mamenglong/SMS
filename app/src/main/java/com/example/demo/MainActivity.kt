package com.example.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.theme.SMSTheme
import com.example.sms.SmsObserver
import com.example.sms.SmsUi


class MainActivity : ComponentActivity() {
    val smsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        it.resultCode
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
                    var sms by rememberSaveable {
                        mutableStateOf(false)
                    }
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (sms){
                            SmsUi(context = this@MainActivity)
                        }else{
                            createButton(text = "短信") {
                                sms = true
                            }
                        }
                    }
                }
            }
        }
        requestPermissions(arrayOf(android.Manifest.permission.BROADCAST_SMS,
            android.Manifest.permission.READ_SMS
            ,android.Manifest.permission.SEND_SMS,android.Manifest.permission.CAMERA),11)

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