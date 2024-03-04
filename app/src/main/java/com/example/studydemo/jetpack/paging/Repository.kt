package com.example.studydemo.jetpack.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.network.RetrofitClient
import com.example.studydemo.net.GitHubService
import kotlinx.coroutines.flow.Flow

object Repository {
    private const val PAGE_SIZE = 50

    private val service by lazy {
        RetrofitClient.createService(
            "https://api.github.com/", GitHubService::class.java
        )
    }

    //Flow是什么，你可以简单将它理解成协程中对标RxJava的一项技术
    //定义了一个getPagingData()函数，这个函数的返回值是Flow<PagingData<Repo>>，注意除了Repo部分是可以改的，其他部分都是固定的。
    //在getPagingData()函数当中，这里创建了一个Pager对象，并调用.flow将它转换成一个Flow对象。
    //在创建Pager对象的时候，我们指定了PAGE_SIZE，也就是每页所包含的数据量。
    //又指定了pagingSourceFactory，并将我们自定义的RepoPagingSource传入，这样Paging 3就会用它来作为用于分页的数据源了。

    fun getPagingData(): Flow<PagingData<Repo>> {
        return Pager(config = PagingConfig(PAGE_SIZE), pagingSourceFactory = {
            RepoPagingSource(
                service
            )
        }).flow
    }
}