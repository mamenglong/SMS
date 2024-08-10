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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForwardSmsService","onStartCommand")
        val notification = NotificationManager.sendCustomNotification(this)
        SmsReceiver.register(this)
        SmsObserver.register(this)
        startForeground(notification.first,notification.second)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("ForwardSmsService","onCreate")
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}