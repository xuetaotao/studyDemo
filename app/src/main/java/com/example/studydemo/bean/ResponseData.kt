package com.example.studydemo.bean

data class ResponseData<T>(
    val errorCode: Int = 0,
    val errorMsg: String? = null,
    var data: T? = null
)