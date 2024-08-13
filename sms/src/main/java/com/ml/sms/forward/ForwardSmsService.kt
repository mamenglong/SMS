package com.ml.sms.forward

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ml.sms.sms.SmsObserver
import com.ml.sms.sms.SmsReceiver

class ForwardSmsService : Service() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ForwardSmsService::class.java)
            context.startForegroundService(intent)
        }
        var isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForwardSmsService","onStartCommand isRunning:$isRunning")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        Log.d("ForwardSmsService","onCreate:$isRunning")
        val notification = NotificationManager.sendCustomNotification(this)
        SmsReceiver.register(this)
        SmsObserver.register(this)
        startForeground(notification.first, notification.second)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}