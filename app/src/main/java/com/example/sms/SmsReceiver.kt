package com.example.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsMessage
import android.util.Log


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
                        // 在此处处理收到的短信
                        Log.d("SmsReceiver","sender:$sender messageBody:$messageBody ")
                    }
                }
            }
        }
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