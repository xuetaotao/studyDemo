package com.example.studydemo.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

//@JvmOverloads主要用于简化Kotlin函数为Java调用生成默认参数方法重载的注解。
//总结@JvmOverloads的作用:
//- 只能用于含默认参数值的函数
//- 会为该函数生成重载方法
//- 方便Java调用者使用默认参数值
//- 不需要写多个方法重载

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 获取控件的高度和宽度
        val width = width
        val height = height
        -lineCount * lineHeight / 2 + lineHeight / 2
    }
}