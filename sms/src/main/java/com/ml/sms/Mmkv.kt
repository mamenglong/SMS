package com.ml.sms

import android.content.Context
import com.tencent.mmkv.MMKV


object Mmkv {
    val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    fun init(context: Context) {
        val rootDir = MMKV.initialize(context)
        System.out.println("mmkv root: $rootDir")
    }
    var dd_token:String
        get(){
           return mmkv.decodeString("dd_token","").toString()
        }
        set(value) {
            mmkv.encode("dd_token",value)
        }
    var dd_secret:String
        get(){
            return mmkv.decodeString("dd_secret","").toString()
        }
        set(value) {
            mmkv.encode("dd_secret",value)
        }
    var dd_tag:String
        get(){
            return mmkv.decodeString("dd_tag","").toString()
        }
        set(value) {
            mmkv.encode("dd_tag",value)
        }
}