package com.example.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PushServiceReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("PushServiceReciever", "onReceive:$intent")
    }
}