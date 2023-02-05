package com.example.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor(baseUrl: String) {

    var retrofit: Retrofit

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    companion object {
        @Volatile
        private var retrofitClient: RetrofitClient? = null

        @JvmStatic
        fun getInstance(baseUrl: String): RetrofitClient {
            if (retrofitClient == null) {
                synchronized(RetrofitClient::class.java) {
                    if (retrofitClient == null) {
                        retrofitClient = RetrofitClient(baseUrl)
                    }
                }
            }
            return retrofitClient!!
        }

        @JvmStatic
        fun <T> createService(baseUrl: String, clazz: Class<T>): T {
            return getInstance(baseUrl).retrofit.create(clazz)
        }
    }
}