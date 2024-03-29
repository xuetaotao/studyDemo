package com.example.studydemo.ui

import androidx.fragment.app.Fragment
import com.example.studydemo.R
import com.example.studydemo.base.BaseFragment
import com.example.studydemo.base.setOnSingleClickListener
import com.example.studydemo.databinding.FragmentMainPageBinding
import com.example.studydemo.jetpack.compose.ComposeTutorialActivity
import com.example.studydemo.jetpack.paging.RepoFragment

class MainPageFragment : BaseFragment<FragmentMainPageBinding>() {
    override fun setLayout(): FragmentMainPageBinding {
        return FragmentMainPageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.btn.setOnSingleClickListener {
//            fragmentReplace(RepoFragment())
            ComposeTutorialActivity.newInstance(requireContext())
        }
    }

    override fun initData() {

    }

    private fun fragmentReplace(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.fragment_container_view, fragment)
        transaction?.hide(this)
        transaction?.show(fragment)
        transaction?.commit()
    }
}