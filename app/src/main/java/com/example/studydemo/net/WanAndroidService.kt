package com.example.studydemo.net

import com.example.studydemo.bean.ResponseData
import com.example.studydemo.bean.UserBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WanAndroidService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginForm(
        @Field("username") username: String,
        @Field("password") password: String
    ): ResponseData<UserBean>
}