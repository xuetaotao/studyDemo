package com.example.studydemo

import android.app.Application
import android.content.Context
import android.util.Log

class App : Application() {

    private lateinit var mContext: Context

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        catchException()
    }

    fun catchException() {
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("Application", "catchException: ${thread.name}\t${throwable.message}")
            defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)
        }
    }
}