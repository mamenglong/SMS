package com.ml.base.app

import android.content.Context
import com.google.auto.service.AutoService

interface AppInit{
    fun init(context: Context)
}
@AutoService(AppInit::class)
class AppInitImpl:AppInit {
    override fun init(context: Context) {

    }
}