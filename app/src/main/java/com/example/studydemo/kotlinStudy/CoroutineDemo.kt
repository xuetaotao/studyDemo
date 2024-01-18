package com.example.studydemo.kotlinStudy

import com.example.studydemo.bean.UserBean
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressWarnings("unused")
class CoroutineDemo {

    @Test
    fun test() {
        coroutineScopeDemo()
    }

    suspend fun withContextDemo() {
        withContext(Dispatchers.IO) {
            delay(1000)
        }
    }

    fun coroutineScopeDemo() {
        runBlocking {
            launch {
                delay(200L)
                letUsPrintln("Task from runBlocking")   // 2. 200 delay  launch 不阻塞
            }

            //如果这里换成launch，结果就不是下面打印的了
//            launch {
            //如果把 coroutineScope 换成 launch. 在100ms之前,上方协程都会因 delay() 进入挂起状态. 所以末尾 over 会最早执行.
            // 但 coroutineScope 它会创建一个协程作用域并且在所有已启动子协程执行完毕之前不会结束; 会等待作用域及其所有子协程执行结束. 相当于job.join()了,
            // 并且当它内部异常时, 作用域内其他子协程将被取消。
            coroutineScope {    // 创建一个协程作用域
                launch {
                    delay(500L)
                    letUsPrintln("Task from nested launch")
                }
                delay(100L)
                letUsPrintln("Task from coroutine scope") // 1. 100 delay  launch 不阻塞
            }

            letUsPrintln("Coroutine scope is over") // 4. 500 delay  coroutineScope
        }
        /* result:
        Task from coroutine scope Thread_name：main @coroutine#1
        Task from runBlocking Thread_name：main @coroutine#2
        Task from nested launch Thread_name：main @coroutine#3
        Coroutine scope is over Thread_name：main @coroutine#1
         */
    }

    fun letUsPrintln(title: String) {
        println("$title, Thread_name：${Thread.currentThread().name}")
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