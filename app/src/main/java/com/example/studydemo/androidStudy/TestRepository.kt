package com.example.studydemo.androidStudy

import com.example.network.RetrofitClient
import com.example.studydemo.net.WanAndroidService

class TestRepository {

    val service by lazy {
        RetrofitClient.createService(
            "https://www.wanandroid.com/", WanAndroidService::class.java
        )
    }
}