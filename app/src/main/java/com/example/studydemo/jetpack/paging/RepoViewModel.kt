package com.example.studydemo.jetpack.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

//将Repository编写完成之后，我们还需要再定义一个ViewModel，因为Activity是不可以直接和Repository交互的，要借助ViewModel才可以。
class RepoViewModel : ViewModel() {
    fun getPagingData(): Flow<PagingData<Repo>> {
        //这里又额外调用了一个cachedIn()函数，这是用于将服务器返回的数据在viewModelScope这个作用域内进行缓存，
        //假如手机横竖屏发生了旋转导致Activity重新创建，Paging 3就可以直接读取缓存中的数据，而不用重新发起网络请求了。
        return Repository.getPagingData().cachedIn(viewModelScope)
    }
}