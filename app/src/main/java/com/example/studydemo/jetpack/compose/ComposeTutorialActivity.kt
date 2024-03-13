package com.example.studydemo.jetpack.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

/**
 * Jetpack Compose 教程
 * https://developer.android.com/jetpack/compose/tutorial?hl=zh-cn
 * 2024.03.13
 */
class ComposeTutorialActivity : AppCompatActivity() {
    companion object {
        fun newInstance(context: Context) {
            context.startActivity(Intent(context, ComposeTutorialActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard("Study Compose Second")
        }
    }

    //如需使函数成为可组合函数，请添加 @Composable 注解
    @Composable
    fun MessageCard(name: String) {
        Text(text = "Hello $name")
    }

    //借助 @Preview 注解，您可以在 Android Studio 中预览可组合函数
    //该注解必须用于不接受参数的可组合函数。因此，您无法直接预览 MessageCard 函数，
    // 而是需要创建另一个名为 PreviewMessageCard 的函数，由该函数使用适当的参数调用 MessageCard
    @Preview
    @Composable
    fun PreviewMessageCard() {
        MessageCard("Study Compose First Ha")
    }
}