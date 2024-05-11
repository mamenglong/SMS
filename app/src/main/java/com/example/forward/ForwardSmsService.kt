package com.example.forward

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.sms.SmsReceiver

class ForwardSmsService : Service() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ForwardSmsService::class.java)
            context.startForegroundService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationManager.sendCustomNotification(this)
        SmsReceiver.register(this)
        startForeground(notification.first,notification.second)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}