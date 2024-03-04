package com.example.studydemo.androidStudy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studydemo.bean.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestViewModel : ViewModel() {

    private val repository = TestRepository()

    private val _userLiveDataResult: MutableLiveData<UserBean> = MutableLiveData<UserBean>()
    val userLiveDataResult: LiveData<UserBean> = _userLiveDataResult

    fun getUser(username: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.service.loginForm(username, password)
            }
            _userLiveDataResult.value = result.data!!
        }
    }
}