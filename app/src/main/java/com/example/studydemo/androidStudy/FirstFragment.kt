package com.example.studydemo.androidStudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studydemo.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //这样写会报错，有问题
//        val view = FragmentFirstBinding.inflate(inflater, container, true)
        val view = FragmentFirstBinding.inflate(layoutInflater)
//        view.tvHello.setOnClickListener {
//            view.tvHello.text = "我要跳了"
//        }
        return view.root
    }

    companion object {
        fun newInstance(): FirstFragment {
            val fragment = FirstFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}