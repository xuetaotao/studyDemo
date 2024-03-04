package com.example.studydemo.androidStudy

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.studydemo.R
import com.example.studydemo.bean.UserBean
import com.example.studydemo.databinding.FragmentTestBinding
import com.example.studydemo.thirdLibrary.PermissionXStudy
import java.util.Calendar

class TestFragment : Fragment() {

    private val viewModel: TestViewModel by viewModels()
    private lateinit var binding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestBinding.inflate(layoutInflater)
        initView()
        initObserver()
        return binding.root
    }

    private fun initView() {
        binding.button.setOnClickListener {
            PermissionXStudy.permissionXTest(this)
//            datePickerDialogUse()
        }
    }

    private fun initObserver() {
        viewModel.userLiveDataResult.observe(viewLifecycleOwner, object : Observer<UserBean> {
            override fun onChanged(value: UserBean) {
                binding.textView.text = value.nickname
            }
        })
    }

    fun netTest() {
        viewModel.getUser("chaozhouzhang", "123456")
    }

    private fun datePickerDialogUse() {
        val calendar = Calendar.getInstance()
        var mYear = calendar.get(Calendar.YEAR)
        var mMonth = calendar.get(Calendar.MONTH)
        var mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this@TestFragment.requireContext(),
            R.style.MySpinnerDatePickerStyle,
            { _, year, month, dayOfMonth ->
                mYear = year
                mMonth = month
                mDay = dayOfMonth
                Toast.makeText(
                    this@TestFragment.context,
                    "${year}年${month + 1}月${dayOfMonth}日",
                    Toast.LENGTH_SHORT
                ).show()
            },
            mYear,
            mMonth,
            mDay
        )
        datePickerDialog.show()
    }
}