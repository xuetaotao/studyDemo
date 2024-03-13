package com.example.studydemo.jetpack.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text

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
            Text("Study Compose First")
        }
    }
}