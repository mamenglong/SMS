package com.ml.sms

import android.app.Application
import android.content.Context
import com.google.auto.service.AutoService
import com.ml.base.app.AppInit
import com.ml.sms.work.LoopWorker

@AutoService(AppInit::class)
class SmsAppInit : AppInit {
    override fun init(context: Context) {
        val processName = Application.getProcessName()
        if (processName==context.packageName) {
            Mmkv.init(context)
            LoopWorker.start(context)
        }
    }

}