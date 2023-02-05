package com.example.studydemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.studydemo.bean.UserBean
import com.example.studydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mainViewModel.userLiveData.observe(this, object : Observer<UserBean> {
            override fun onChanged(t: UserBean?) {
                mBinding.textView.text = t?.nickname
            }
        })

        mBinding.button.setOnClickListener {
            mainViewModel.getUser("chaozhouzhang", "123456")
        }
    }
}