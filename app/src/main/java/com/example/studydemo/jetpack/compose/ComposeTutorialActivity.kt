package com.example.studydemo.jetpack.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studydemo.R

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
            MessageCard(Message("Android", "Jetpack Compose"))
        }
    }

    data class Message(val author: String, val body: String)

    //如需使函数成为可组合函数，请添加 @Composable 注解
    @Composable
    fun MessageCard(msg: Message) {
        //使用 Row 水平排列各项
        //Add padding around our message,
        //Compose 使用了修饰符。通过修饰符，您可以更改可组合项的大小、布局、外观，还可以添加高级互动，例如使元素可点击
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            //Column 函数可让您垂直排列元素。
            Column {
                Text(text = msg.author)
                // Add a vertical space between the author and message texts
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = msg.body)
            }
        }
    }

    //借助 @Preview 注解，您可以在 Android Studio 中预览可组合函数
    //该注解必须用于不接受参数的可组合函数。因此，您无法直接预览 MessageCard 函数，
    // 而是需要创建另一个名为 PreviewMessageCard 的函数，由该函数使用适当的参数调用 MessageCard
    @Preview
    @Composable
    fun PreviewMessageCard() {
        MessageCard(Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!"))
    }
}