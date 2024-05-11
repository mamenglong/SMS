package com.example.forward

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sms.R

object NotificationManager {
    fun sendCustomNotification(context: Context): Pair<Int, Notification> {
        // 创建通知渠道
        val channelId = "my_channel_id"
        val channelName = "短信转发"
        val channelDescription = "转发短信消息"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
            .apply {
                setDescription(channelDescription)
            }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        // 创建通知
        val title = "短信转发"
        val message = "This is a custom notification"
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 设置通知点击操作
        val intent = Intent("android.intent.action.MAIN")
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

        // 构建通知对象
        val notification = builder.build()

        // 发送通知
        val notificationId = 1
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "请开启通知权限!", Toast.LENGTH_SHORT).show()
            gotoNotificationSetting(context)
        } else {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
        return notificationId to notification
    }


    fun gotoNotificationSetting(context: Context) {
        try {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
//            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "your_channel_id")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.setData(uri)
            context.startActivity(intent)
        }
    }
}