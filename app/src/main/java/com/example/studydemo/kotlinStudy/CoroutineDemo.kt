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
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@SuppressWarnings("unused")
class CoroutineDemo {

    @Test
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
        }/* result:
        Task from coroutine scope Thread_name：main @coroutine#1
        Task from runBlocking Thread_name：main @coroutine#2
        Task from nested launch Thread_name：main @coroutine#3
        Coroutine scope is over Thread_name：main @coroutine#1
         */
    }

    private fun letUsPrintln(title: String) {
        println("$title, Thread_name：${Thread.currentThread().name}")
    }

    //没看懂为啥? 额，这不就是类似网络请求拿到结果后再更新UI。。
    @Test
    fun withContextDemo() {
        runBlocking {
            letUsPrintln("start!-主线程")
            withContext(Dispatchers.IO) {
                delay(1000L)
                letUsPrintln("111-子线程!")
            }
            letUsPrintln("end!-主线程")
        }
    }/*
    start!-主线程, Thread_name：main @coroutine#1
    111-子线程!, Thread_name：DefaultDispatcher-worker-1 @coroutine#1
    end!-主线程, Thread_name：main @coroutine#1
     */

    //sampleStart
// Sequentially executes doWorld followed by "Done"
    @Test
    fun main2() = runBlocking {
        doWorld()
        println("Done")
    }

    // Concurrently executes both sections
    suspend fun doWorld() = coroutineScope { // this: CoroutineScope
        launch {
            delay(2000L)
            println("World 2")
        }
        launch {
            delay(1000L)
            println("World 1")
        }
        println("Hello")
    }
//sampleEnd


    @Test
    fun main3() = runBlocking {
        repeat(50_000) { // 启动大量的协程
            launch {
                delay(5000L)
                print(".")
            }
        }
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

    @Test
    fun testWithout() {
        runBlocking {
            val result = withTimeoutOrNull(10000) {
                repeat(1000) { i ->
                    println("I'm sleeping $i times")
                    delay(500)
                }
                "Done"
            }
            println("Result is $result")
        }
    }

    @Test
    fun test() {
        runBlocking {
            letUsPrintln("start!-主线程")

            withContext(Dispatchers.IO) { // 启动一个新协程, 这是 this.launch
                delay(1000L)
                letUsPrintln("111-子线程!")
            }
            letUsPrintln("end!-主线程")
        }
    }
}