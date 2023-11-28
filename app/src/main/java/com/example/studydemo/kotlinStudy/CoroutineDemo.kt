package com.example.studydemo.kotlinStudy

import com.example.studydemo.bean.UserBean
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressWarnings("unused")
class CoroutineDemo {

    suspend fun withContextDemo() {
        withContext(Dispatchers.IO) {
            delay(1000)
        }
    }

    fun coroutineScopeDemo(){

    }

    suspend fun suspendCoroutineDemo() {
        suspendCoroutine<UserBean> { continuation ->
            val stu = UserBean("aa", "bb", "cc", "dd", 2)
            continuation.resume(value = stu)
            continuation.resumeWith(Result.success(stu))
            continuation.resumeWithException(Exception("hey"))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun suspendCancellableCoroutineDemo() {
        suspendCancellableCoroutine<UserBean> { cancellableContinuation ->
            val cancelled = cancellableContinuation.isCancelled
            val stu = UserBean("aa", "bb", "cc", "dd", 2)
            cancellableContinuation.resume(stu)
            cancellableContinuation.resumeWith(Result.success(stu))
            cancellableContinuation.resumeWithException(Exception("hey"))
            cancellableContinuation.cancel(Exception("hey2"))
        }
        GlobalScope.launch {
            //origin code use suspendCancellableCoroutine too.
            delay(100)
        }
    }

    class ContinuationImpl(override val context: CoroutineContext) : Continuation<UserBean> {
        override fun resumeWith(result: Result<UserBean>) {
            println()
        }
    }
}