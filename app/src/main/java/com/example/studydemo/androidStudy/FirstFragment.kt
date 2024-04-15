package com.example.studydemo.androidStudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studydemo.R
import com.example.studydemo.databinding.FragmentFirstBinding
import com.umeng.analytics.MobclickAgent

class FirstFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        //这样写会报错，有问题
//        val view = FragmentFirstBinding.inflate(inflater, container, true)
        val view = FragmentFirstBinding.inflate(layoutInflater)
        view.tvHello.setOnClickListener {
//            view.tvHello.text = "我要跳了"
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view, SecondFragment())
                ?.addToBackStack("FirstFragment") // 可选，将当前 Fragment 添加到返回栈
                ?.commit()
        }
        return view.root
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart("FirstFragment")
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd("FirstFragment")
    }

    companion object {
        fun newInstance(): FirstFragment {
            val fragment = FirstFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}