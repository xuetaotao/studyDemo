package com.example.studydemo.androidStudy

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.studydemo.databinding.ActivityAndroid14Binding
import com.example.studydemo.utils.showLog
import com.example.studydemo.utils.showToast


class Android14Activity : AppCompatActivity() {

    private lateinit var binding: ActivityAndroid14Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndroid14Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClick.setOnClickListener {
//            showToast(this, "Android14Activity 适配测试")
            localBroadcastManager(this)
        }
    }

    private fun localBroadcastManager(context: Context) {
        showLog("---localBroadcastManager---")
        LocalBroadcastManager.getInstance(context).registerReceiver(
            customReceiver,
            IntentFilter(ACTION_DYNAMIC_BROADCAST)
        )
        val intent = Intent(ACTION_DYNAMIC_BROADCAST)
        Handler(Looper.getMainLooper()).postDelayed({
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }, 1000)
    }

    companion object {
        /**
         * 安全性：对隐式 intent 和待处理 intent 的限制
         *
         * 1）隐式 intent 只能传送到导出的组件。应用必须使用显式 intent 传送到未导出的组件，或将该组件标记为已导出。
         * (原文：Implicit intents are only delivered to exported components. Apps must either use an explicit intent to deliver to unexported components,
         * or mark the component as exported.)
         *
         * 显式Intent直接指定目标组件的类名，而隐式Intent通过指定动作、数据和类别等信息，让系统根据这些信息来查找合适的组件来处理该Intent。
         */
        fun startAndroid14Activity1(context: Context) {
            //隐式 intent 只能传送到导出的组件。应用必须使用显式 intent 传送到未导出的组件，或将该组件标记为已导出。
            //Test 1: targeting Android 14, run on Android 14, Android14Activity exported="false", 崩溃, catchException: main	No Activity found to handle Intent { act=com.example.action.APP_ACTION }
            //Test 2: targeting Android 14, run on Android 14, Android14Activity exported="true", running ok.
            //Test 3: targeting Android 14, run on Android 13, Android14Activity exported="false", running ok.
            val intent = Intent("com.example.action.APP_ACTION")
            context.startActivity(intent)
        }

        //正确的启动方式
        fun startAndroid14Activity2(context: Context) {
            //如需启动非导出的 activity，应用应改用显式 intent
            //Test 4: targeting Android 14, run on Android 14/13, Android14Activity exported="false", running ok.
            val intent = Intent("com.example.action.APP_ACTION").apply {
                //context.packageName(Return the name of this application's package): com.example.studydemo
                setPackage(context.packageName)
            }
            context.startActivity(intent)
        }

        /**
         * 安全性：对隐式 intent 和待处理 intent 的限制
         *
         * 2）如果应用通过未指定组件或软件包的 intent 创建可变待处理 intent，系统会抛出异常。
         * (原文：If an app creates a mutable pending intent with an intent that doesn't specify a component or package, the system throws an exception.）
         *
         * Test 4: targeting Android 14, run on Android 14, 崩溃.
         * Test 5: targeting Android 14, run on Android 13, 不崩溃，但是也发不出去广播.
         *
         * 修改：
         * 将PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE 改成
         * PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE，不再报红，报error
         *
         * Test 6: targeting Android 14, run on Android 14, 不崩溃，但是也发不出去广播.
         * Test 7: targeting Android 14, run on Android 13, 不崩溃，但是也发不出去广播.
         *
         * java.lang.IllegalArgumentException:
         * com.example.studydemo: Targeting U+ (version 34 and above) disallows creating or
         * retrieving a PendingIntent with FLAG_MUTABLE, an implicit Intent within and without FLAG_NO_CREATE and
         * FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT for security reasons. To retrieve an already existing PendingIntent, use FLAG_NO_CREATE,
         * however, to create a new PendingIntent with an implicit Intent use FLAG_IMMUTABLE.
         */
        fun startAndroid14Activity3(context: Context) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent("com.example.action.Android14_Broadcast"),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            //当前线程的 looper
            val handler = Looper.myLooper()?.let { Handler(it) }
//            val handler = Handler(Looper.getMainLooper())//Main线程的
            handler?.post {
                showLog("Thread: ${Thread.currentThread().name}")
                pendingIntent.send()
            }
        }

        /**
         * 解决startAndroid14Activity3的问题：
         * 使用指定组件或软件包的 intent 创建可变待处理 intent
         *
         * Test 8: targeting Android 14, run on Android 14, 不崩溃，running ok.
         * Test 9: targeting Android 14, run on Android 13, 不崩溃，running ok.
         */
        fun startAndroid14Activity4(context: Context) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent("com.example.action.Android14_Broadcast")
                    .apply {
                        setPackage(context.packageName)
                    },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            //当前线程的 looper
            val handler = Looper.myLooper()?.let { Handler(it) }
//            val handler = Handler(Looper.getMainLooper())//Main线程的
            handler?.postDelayed({
                showLog("Thread: ${Thread.currentThread().name}")
                pendingIntent.send()
            }, 500)
        }

        private const val ACTION_DYNAMIC_BROADCAST = "dynamic broadcast register"

        private val customReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                showLog("intent?.action: ${intent?.action}")
                if (ACTION_DYNAMIC_BROADCAST != intent?.action) {
                    return
                }
//                if (context != null) {
//                    release(context)
//                }
                showToast(context, "intent?.action: ${intent.action}")
            }
        }

        /**
         * 以 Android 14（API 级别 34）或更高版本为目标平台并使用上下文注册的接收器的应用和服务必须指定一个标志，
         * 以指明接收器是否应导出到设备上的所有其他应用：分别为RECEIVER_EXPORTED 或 RECEIVER_NOT_EXPORTED。
         *
         * Test 10: targeting Android 14, run on Android 14, 崩溃.
         * catchException: main	com.example.studydemo: One of RECEIVER_EXPORTED or RECEIVER_NOT_EXPORTED should
         * be specified when a receiver isn't being registered exclusively for system broadcasts
         *
         * Test 11: targeting Android 14, run on Android 13, 不崩溃，running ok.
         */
        fun dynamicBroadcast(context: Context) {
            context.registerReceiver(customReceiver, IntentFilter(ACTION_DYNAMIC_BROADCAST))
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ACTION_DYNAMIC_BROADCAST).apply {
                    setPackage(context.packageName)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            Handler(Looper.getMainLooper()).postDelayed({
                pendingIntent.send()
            }, 100)
        }

        /**
         * Test 12: targeting Android 14, run on Android 14, 不崩溃，running ok.
         * Test 13: targeting Android 14, run on Android 13, 不崩溃，running ok.
         */
        fun dynamicBroadcast2(context: Context) {
            ContextCompat.registerReceiver(
                context, customReceiver, IntentFilter(ACTION_DYNAMIC_BROADCAST),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ACTION_DYNAMIC_BROADCAST).apply {
                    setPackage(context.packageName)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            Handler(Looper.getMainLooper()).postDelayed({
                pendingIntent.send()
            }, 100)
        }

        private val batteryChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                showLog("intent?.action: ${intent?.action}")
//                context?.let {
//                    release(it)
//                }
                showToast(context, "intent?.action: ${intent?.action}")
            }
        }

        /**
         * 仅接收系统广播的接收器的例外情况
         * 如果您的应用仅通过Context#registerReceiver 方法（例如 Context#registerReceiver()）为系统广播注册接收器，那么在注册接收器时不应指定标志。
         * 实际测试情况是加不加RECEIVER_NOT_EXPORTED 都 ok
         * Test 14: targeting Android 14, run on Android 14, 不崩溃，running ok.
         * Test 15: targeting Android 14, run on Android 13, 不崩溃，running ok.
         */
        fun dynamicSystemBroadcast(context: Context) {
            context.registerReceiver(
                batteryChangedReceiver,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
//            ContextCompat.registerReceiver(
//                context, batteryChangedReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED),
//                ContextCompat.RECEIVER_NOT_EXPORTED
//            )
        }

        //用完记得释放
        fun release(context: Context) {
            context.unregisterReceiver(customReceiver)
            context.unregisterReceiver(batteryChangedReceiver)
        }
    }
}