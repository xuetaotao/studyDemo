package com.example.studydemo.androidStudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studydemo.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //这样写会报错，有问题
//        val view = FragmentSecondBinding.inflate(inflater, container, true)
        val view = FragmentSecondBinding.inflate(layoutInflater)
        return view.root
    }

    companion object {
        fun newInstance(): SecondFragment {
            val fragment = SecondFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}