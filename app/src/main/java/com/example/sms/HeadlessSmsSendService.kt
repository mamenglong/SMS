package com.example.sms

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class HeadlessSmsSendService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        Log.d("HeadlessSmsSendService","onBind:$intent")
        return null
    }
}