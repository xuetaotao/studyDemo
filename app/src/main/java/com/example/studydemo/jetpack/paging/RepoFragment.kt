package com.example.studydemo.jetpack.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studydemo.databinding.FragmentRepoBinding
import kotlinx.coroutines.launch

/**
 * 郭霖原创：Jetpack新成员，Paging3从吐槽到真香
 * https://blog.csdn.net/guolin_blog/article/details/114707250
 */
class RepoFragment : Fragment() {

    //    private val viewModel by lazy { ViewModelProvider(this)[RepoViewModel::class.java] }
    private val viewModel: RepoViewModel by viewModels()

    private lateinit var binding: FragmentRepoBinding
    private val repoAdapter by lazy { RepoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }

    private fun initView() {
        val recyclerView = binding.recyclerView
        val progressBar = binding.progressBar
        recyclerView.layoutManager = LinearLayoutManager(this@RepoFragment.context)
        recyclerView.adapter = repoAdapter
        lifecycleScope.launch {
            viewModel.getPagingData().collect { pagingData ->
                //这里最重要的一段代码就是调用了RepoAdapter的submitData()函数。这个函数是触发Paging 3分页功能的核心，调用这个函数之后，Paging 3就开始工作了。
                //submitData()接收一个PagingData参数，这个参数我们需要调用ViewModel中返回的Flow对象的collect()函数才能获取到，
                //collect()函数有点类似于Rxjava中的subscribe()函数，总之就是订阅了之后，消息就会源源不断往这里传。
                //不过由于collect()函数是一个挂起函数，只有在协程作用域中才能调用它，因此这里又调用了lifecycleScope.launch()函数来启动一个协程。
                repoAdapter.submitData(pagingData)
            }
        }
        repoAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    progressBar.visibility = View.INVISIBLE
                    recyclerView.visibility = View.VISIBLE
                }

                is LoadState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.INVISIBLE
                }

                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@RepoFragment.context,
                        "Load Error: ${state.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}