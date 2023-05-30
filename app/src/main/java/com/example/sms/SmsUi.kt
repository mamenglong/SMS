package com.example.sms

import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Telephony
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.example.demo.MainActivity
import com.example.demo.SmsActivity
import com.example.demo.createButton

@Composable
fun SmsUi(context: MainActivity) {
    createButton(text = "默认") {
        launch(context)
    }
    createButton(text = "查询能接受短信的应用") {
        openSMSappChooser(context)
    }
    createButton(text = "默认应用设置") {
        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, "default");
        context.startActivity(intent)
    }
    createButton(text = "测试发送短信") {
        val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
//        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
//        intent.putExtra(Settings.EXTRA_CHANNEL_ID, "default");
//        context.startActivity(intent)
        // context.startActivity(Intent(context,SettingsActivity::class.java))
        if (isDefault(context)) {

        } else {
            launch(context)
        }
    }
    createButton(text = "卸载") {
        val uri = Uri.fromParts("package", context.packageName, null)
        val intent = Intent(Intent.ACTION_DELETE, uri)
        context.startActivity(intent)
    }
}

fun launch(context: MainActivity) {
    val isD = isDefault(context)
    Log.d("mml", "isDefault:${isD}")
    if (isD) {
        Toast.makeText(context, "已经是默认短信了", Toast.LENGTH_SHORT).show()
        return
    }
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
        Log.d("mml", "isRoleAvailable:${roleManager.isRoleAvailable(RoleManager.ROLE_SMS)}")
        roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS)
    } else {
        Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).putExtra(
            "package",
            context.getPackageName()
        )
    }
    runCatching {
        context.smsResultLauncher.launch(intent)
    }.onFailure {
        it.printStackTrace()
    }


}

fun isDefault(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        (context.getSystemService(RoleManager::class.java) as RoleManager).isRoleHeld(RoleManager.ROLE_SMS)
    } else TextUtils.equals(
        Telephony.Sms.getDefaultSmsPackage(context),
        context.packageName
    )
}

fun openSMSappChooser(context: Context) {
    val packageManager = context.packageManager
    val componentName = ComponentName(context, SmsActivity::class.java)
    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
    val selector = Intent(Intent.ACTION_MAIN)
    selector.addCategory(Intent.CATEGORY_APP_MESSAGING)
    selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(selector)
    packageManager.setComponentEnabledSetting(
        componentName,
        PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
        PackageManager.DONT_KILL_APP
    )
}