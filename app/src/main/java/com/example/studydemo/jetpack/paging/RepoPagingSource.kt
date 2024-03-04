package com.example.studydemo.jetpack.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.studydemo.net.GitHubService

//在继承PagingSource时需要声明两个泛型类型，第一个类型表示页数的数据类型，我们没有特殊需求，所以直接用整型就可以了。
//第二个类型表示每一项数据（注意不是每一页）所对应的对象类型，这里使用刚才定义的Repo
class RepoPagingSource(private val gitHubService: GitHubService) : PagingSource<Int, Repo>() {

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        //属于Paging 3比较高级的用法，我们本篇文章涉及不到，所以直接返回null就可以了
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            //通过params参数得到key，这个key就是代表着当前的页数
            //注意key是可能为null的，如果为null的话，我们就默认将当前页数设置为第一页。
            val page = params.key ?: 1
            //通过params参数得到loadSize，表示每一页包含多少条数据，这个数据的大小我们可以在稍后设置
            val pageSize = params.loadSize
            val repoResponse = gitHubService.searchRepos(page, pageSize)
            val repoItems = repoResponse.items
            val prevKey = if (page > 1) {
                page - 1
            } else null
            val nextKey = if (repoItems.isNotEmpty()) {
                page + 1
            } else null
            //注意LoadResult.Page()函数接收3个参数，第一个参数传入从响应数据解析出来的Repo列表即可，
            //第二和第三个参数分别对应着上一页和下一页的页数。针对于上一页和下一页，
            //我们还额外做了个判断，如果当前页已经是第一页或最后一页，那么它的上一页或下一页就为null
            LoadResult.Page<Int, Repo>(repoItems, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}