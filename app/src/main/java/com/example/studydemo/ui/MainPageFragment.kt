package com.example.studydemo.ui

import androidx.fragment.app.Fragment
import com.example.studydemo.R
import com.example.studydemo.androidStudy.Android14Activity
import com.example.studydemo.androidStudy.MediaFragment
import com.example.studydemo.base.BaseFragment
import com.example.studydemo.base.setOnSingleClickListener
import com.example.studydemo.databinding.FragmentMainPageBinding
import com.example.studydemo.utils.showLog

class MainPageFragment : BaseFragment<FragmentMainPageBinding>() {
    override fun setLayout(): FragmentMainPageBinding {
        return FragmentMainPageBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.btn.setOnSingleClickListener {
//            fragmentReplace(FirstFragment())
//            ComposeTutorialActivity.newInstance(requireContext())
//            fragmentReplace(MediaFragment())
            fragmentAdd(MediaFragment(), "MediaFragment")
//            Android14Activity.startAndroid14Activity2(requireContext())
        }
    }

    override fun initData() {
//        fragmentManager()
    }

    private fun fragmentManager() {
        val activityFragmentManager = activity?.supportFragmentManager
        val parentFragmentManager = parentFragmentManager
        val childFragmentManager = childFragmentManager
        showLog(
            "activityFragmentManager: $activityFragmentManager, " +
                    "parentFragmentManager: $parentFragmentManager, " +
                    "childFragmentManager: $childFragmentManager"
        )
        /**
         * activityFragmentManager: FragmentManager{3d75270 in HostCallbacks{6c69e9}}},
         * parentFragmentManager: FragmentManager{3d75270 in HostCallbacks{6c69e9}}},
         * childFragmentManager: FragmentManager{9a4466e in MainPageFragment{f7bb10f}}}
         */
    }

    /**
     * add fragment with no containerViewId
     */
    private fun fragmentAdd(fragment: Fragment, tag: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(fragment, tag)
        transaction?.hide(this)
        transaction?.show(fragment)
        transaction?.commit()
    }

    private fun fragmentReplace(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.add(R.id.fragment_container_view, fragment)
        transaction?.hide(this)
        transaction?.show(fragment)
        transaction?.commit()
    }
}