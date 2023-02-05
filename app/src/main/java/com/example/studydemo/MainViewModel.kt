package com.example.studydemo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.RetrofitClient
import com.example.studydemo.bean.UserBean
import com.example.studydemo.net.WanAndroidService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    var userLiveData = MutableLiveData<UserBean>()
    val service by lazy {
        RetrofitClient.createService(
            "https://www.wanandroid.com/",
            WanAndroidService::class.java
        )
    }

    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                service.loginForm(username, password)
            }
            userLiveData.value = result.data!!
        }
    }
}