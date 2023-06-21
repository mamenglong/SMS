package com.example.sms

import android.app.role.RoleManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.demo.MainActivity

object SmsUtil {
    fun mockSms(context: Context, item: MsgItem): Boolean {
        kotlin.runCatching {
            val parse = Uri.parse("content://sms/${item.type}")
            val contentValues = ContentValues()
            contentValues.put("address", item.address)
            contentValues.put("body", item.body)
            contentValues.put("read", Integer.valueOf(if (item.isRead) 1 else 0))
            contentValues.put("date", item.insertTime())
            val uri = context.contentResolver.insert(parse, contentValues)
            Log.d("SmsUtil", "模拟成功:${uri}")
            return true
        }.onFailure {
            Log.d("SmsUtil", "模拟失败:${it}")
        }
        return false
    }

    fun deleteAll(context: MainActivity) {
        runCatching {
            val parse = Uri.parse("content://sms/")
            val cr = context.contentResolver
            val cmd = cr.delete(parse, null, null)
            Toast.makeText(context, "删除${cmd}条", Toast.LENGTH_SHORT).show()
        }.onFailure {
            Log.d("SmsUtil","deleteAll:${it}")
        }
    }

    fun setDefaultSms(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            Log.d("SmsUtil", "isRoleAvailable:${roleManager.isRoleAvailable(RoleManager.ROLE_SMS)}")
            if (roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
            } else {
                Toast.makeText(context, "本机不支持设置默认短信", Toast.LENGTH_SHORT).show()
                return
            }
        } else {
            Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).putExtra(
                "package",
                context.packageName
            )
        }
        runCatching {
            launcher.launch(intent)
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun isDefault(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            (context.getSystemService(RoleManager::class.java) as RoleManager).isRoleHeld(
                RoleManager.ROLE_SMS
            )
        } else TextUtils.equals(
            Telephony.Sms.getDefaultSmsPackage(context),
            context.packageName
        )
    }

    fun openSMSappChooser(context: Context) {
        val selector = Intent(Intent.ACTION_MAIN)
        selector.addCategory(Intent.CATEGORY_APP_MESSAGING)
        selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(selector)
    }

}