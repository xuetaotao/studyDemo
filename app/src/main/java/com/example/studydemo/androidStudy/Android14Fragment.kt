package com.example.studydemo.androidStudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.studydemo.databinding.FragmentAndroid14Binding

class Android14Fragment : Fragment() {

    private lateinit var binding: FragmentAndroid14Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAndroid14Binding.inflate(layoutInflater)
        binding.btnClick.setOnClickListener {

        }
        return binding.root
    }
}