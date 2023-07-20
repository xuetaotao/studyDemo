package com.example.studydemo

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.studydemo.bean.UserBean
import com.example.studydemo.databinding.ActivityMainBinding
import java.util.Calendar

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
//            PermissionXStudy.permissionXTest(this)
            datePickerDialogUse()
        }
    }

    private fun datePickerDialogUse() {
        val calendar = Calendar.getInstance()
        var mYear = calendar.get(Calendar.YEAR)
        var mMonth = calendar.get(Calendar.MONTH)
        var mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.MySpinnerDatePickerStyle,
            { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                Toast.makeText(
                    this@MainActivity,
                    "${year}年${month + 1}月${dayOfMonth}日",
                    Toast.LENGTH_SHORT
                ).show()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    fun netTest() {
        mainViewModel.getUser("chaozhouzhang", "123456")
    }
}