package com.example.sms

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

class SmsObserver(val context: Context,handler: Handler): ContentObserver(handler) {
    companion object{
        fun register(context: Context){
            SmsObserver(context,Handler(Looper.getMainLooper())).also {
                context.contentResolver.registerContentObserver(Uri.parse("content://sms/"), true,
                    it)
            }
        }
    }
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (uri.toString().equals("content://sms/raw")){
            return
        }
        if (uri.toString().contains("content://sms/")){
            kotlin.runCatching {
                getSms(uri)
            }.onFailure {
                Log.d("SmsObserver","onFailure:$it")
            }
        }
        Log.d("SmsObserver","uri:${uri.toString()}")
    }

    fun getSms(smsUri: Uri?) {
        val cr = context.contentResolver
        val projection = arrayOf(
            "_id", "address",
            "person", "body",
            "date", "type",
            "read", "status",
            "date", "thread_id"
        )
        val cur = cr.query(smsUri!!, projection, null, null, "date desc")
        if (cur != null && cur.moveToFirst()) {
            val _id = cur.getString(cur.getColumnIndex("_id")) // 手机号
            val phone = cur.getString(cur.getColumnIndex("address")) // 手机号
            val name = cur.getString(cur.getColumnIndex("person")) ?: "" // 联系人姓名列表
            val body = cur.getString(cur.getColumnIndex("body")) ?: "null" // 短信内容
            val type = cur.getInt(cur.getColumnIndex("type"))
            val read = cur.getInt(cur.getColumnIndex("read"))
            val status = cur.getInt(cur.getColumnIndex("status"))
            val date = cur.getLong(cur.getColumnIndex("date"))
            val thread = cur.getInt(cur.getColumnIndex("thread_id"))
            Toast.makeText(context, "检测到短信插入:${phone}-${body}", Toast.LENGTH_SHORT).show()
        }
    }
}