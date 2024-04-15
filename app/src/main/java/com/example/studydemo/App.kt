package com.example.studydemo

import android.app.Application
import android.content.Context
import android.util.Log
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class App : Application() {

    private lateinit var mContext: Context

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        catchException()
        initThirdSdk()
    }

    private fun initThirdSdk() {
        initUmengSdk()
    }

    private fun initUmengSdk() {
        val appKey = "66180041cac2a664de1b35e3"
        UMConfigure.setLogEnabled(true)
        UMConfigure.preInit(this, appKey, "Umeng-Debug")
        UMConfigure.init(
            this, appKey, "Umeng-Debug", UMConfigure.DEVICE_TYPE_PHONE, ""
        )
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)
    }

    private fun catchException() {
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("Application", "catchException: ${thread.name}\t${throwable.message}")
            defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)
        }
    }
}