package com.ml.sms.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ml.sms.R
import com.ml.sms.forward.ForwardSmsService
import java.time.Duration

class LoopWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    companion object {
        fun start(context: Context) {
            WorkManager.getInstance(context)
                .enqueue(
                    PeriodicWorkRequest
                        .Builder(LoopWorker::class.java, Duration.ofHours(2))
                        .build()
                )
        }
    }

    override fun doWork(): Result {
        val context = applicationContext
        makeStatusNotification("短信定时任务开启", context)
        return kotlin.runCatching {
            ForwardSmsService.start(context)

        }.fold(
            onSuccess = {
                makeStatusNotification("短信定时任务启动成功",context)
                Result.success()
            },
            onFailure = {
                makeStatusNotification("短信定时任务启动失败",context)
                Result.failure()
            }
        )

    }

    private fun makeStatusNotification(message: String, context: Context) {
        val CHANNEL_ID = "短信定时唤醒"
        val NOTIFICATION_ID = 233
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "定时唤醒"
            val description = "定时唤醒定时唤醒短信服务"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }

        // Create the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_timer_24)
            .setContentTitle("WorkRequest Starting")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        // Show the notification
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }

}