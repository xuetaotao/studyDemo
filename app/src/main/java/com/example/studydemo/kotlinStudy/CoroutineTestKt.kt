package com.example.studydemo.kotlinStudy

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 协程的并发问题处理
 * 回调写法转为同步写法
 */
class CoroutineTestKt {

    @Test
    fun test() {
        main7()
    }

    /**
     * 这里launch就启动了一个协程，它只在一个线程中运行，所以不存在并发问题。
     */
    private fun main() = runBlocking {
        //共享变量
        var i = 0
        //指定Default 线程池
        launch(Dispatchers.Default) {
            repeat(1000) {
                println("i = $i-->${Thread.currentThread().name}")
                i++
                //delay(10)//加不加测试结果都一样
            }
        }
        delay(1000L)
        println("result i = $i-->${Thread.currentThread().name}")
    }

    /**
     * 一定要分清楚线程和协程的关系，同时要记住并发问题的原因是多线程。
     * 这里依旧在Default线程池上开启了10个协程，而且每个协程都自增1000次，会发现这里的打印结果会不到10000，
     * 子协程所运行的线程是不一样的，也正是因为这样，多个线程同时访问共享变量时就会出现并发问题。
     */
    private fun main2() = runBlocking {
        //共享变量
        var i = 0
        //句柄集合
        val jobs = mutableListOf<Job>()
        //重复10次
        repeat(10) {
            val job = launch(Dispatchers.Default) {
                repeat(1000) {
                    i++
                }
                println("job 线程-->${Thread.currentThread().name}")
            }
            jobs.add(job)
        }
        //等待计算完成
        jobs.joinAll()
        println("i = $i-->${Thread.currentThread().name}")
    }

    /**
     * 这里对i++这个操作用synchronized给包裹起来了，synchronized时可以保证同时只有一个线程访问临界区，同时lock的解锁Happens Before对lock的加锁，所以这里的结果就是10000。
     */
    private fun main3() = runBlocking {
        var i = 0
        val jobs = mutableListOf<Job>()
        val lock = Any()

        //重复十次
        repeat(10) {
            val job = launch(Dispatchers.Default) {
                repeat(1000) {
                    //使用synchronized锁住代码块
                    synchronized(lock) {
                        //delay(10)//挂起点不能在一个临界区内
                        i++
                    }
                }
                println("job 线程-->${Thread.currentThread().name}")
            }
            jobs.add(job)
        }
        // 等待计算完成
        jobs.joinAll()
        println("i = $i-->${Thread.currentThread().name}")
    }

    /**
     * AtomicInteger
     */
    private fun main4() = runBlocking {
        val i = AtomicInteger(0)
        val jobs = mutableListOf<Job>()

        //重复十次
        repeat(10) {
            val job = launch(Dispatchers.Default) {
                repeat(1000) {
                    //以原子方式递增当前值并返回旧值。它相当于i ++操作(i ++ 是先把变量i的值赋值给左边变量，然后再把变量i的值加1)
                    val getAndIncrement: Int = i.getAndIncrement()
                }
                println("job 线程-->${Thread.currentThread().name}")
            }
            jobs.add(job)
        }
        // 等待计算完成
        jobs.joinAll()
        println("i = $i-->${Thread.currentThread().name}")
    }

    //单线程线程池的CoroutineDispatcher
    val mySingleDispatcher = Executors.newSingleThreadExecutor {
        Thread(it, "MySingleThread").apply {
            isDaemon = true
        }
    }.asCoroutineDispatcher()

    /**
     * 单线程并发多协程
     */
    private fun main5() = runBlocking {
        var i = 0
        val jobs = mutableListOf<Job>()

        //重复十次
        repeat(10) {
            val job = launch(mySingleDispatcher) {
                repeat(1000) {
                    i++
                }
                println("job 线程-->${Thread.currentThread().name}")
            }
            jobs.add(job)
        }
        // 等待计算完成
        jobs.joinAll()
        println("i = $i-->${Thread.currentThread().name}")
    }

    /**
     * 非阻塞的锁：Mutex
     */
    private fun main6() = runBlocking {
        var i = 0
        val jobs = mutableListOf<Job>()
        //协程的锁对象
        val mutex = Mutex()

        // 重复十次
        repeat(10) {
            val job = launch(Dispatchers.Default) {
                repeat(1000) {
                    mutex.withLock {
                        i++
                    }
                }
                println("job 线程-->${Thread.currentThread().name}")
            }
            jobs.add(job)
        }
        // 等待计算完成
        jobs.joinAll()
        println("i = $i-->${Thread.currentThread().name}")
    }


    private fun main7() = runBlocking {
        val result = requestBaiDu()
        println("result is：$result")
    }

    /**
     * 将异步回调结果的写法改成同步形式
     */
    private suspend fun requestBaiDu(): String {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("http://wwww.baidu.com")
            .build()
        val call = okHttpClient.newCall(request)
        return suspendCancellableCoroutine { cancellableContinuation ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val msg =
                        (e.message ?: "request fail") + "\nThread：\n" + Thread.currentThread().name
//                    cancellableContinuation.resume(msg)
                    cancellableContinuation.resumeWithException(Exception(msg))
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = (response.body?.string()
                        ?: "request result is empty") + "\nThread：\n" + Thread.currentThread().name
                    cancellableContinuation.resume(result)
                }
            })
        }
    }
}