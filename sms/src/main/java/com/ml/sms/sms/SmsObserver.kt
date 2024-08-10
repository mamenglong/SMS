package com.ml.sms.sms

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.ml.sms.Mmkv
import com.ml.sms.http.HttpManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
            val msg = "${Mmkv.dd_tag} 于 ${timeFormatter(date)} 收到 $phone 的短信\n内容: $body"
            HttpManager.upload(msg)
            Toast.makeText(context, "检测到短信插入:${phone}-${body}", Toast.LENGTH_SHORT).show()
        }
    }
    fun timeFormatter(timestamp:Long):String{
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // 将 LocalDateTime 转换为字符串
        val formattedDateTime = localDateTime.format(formatter)
        return formattedDateTime
    }

}