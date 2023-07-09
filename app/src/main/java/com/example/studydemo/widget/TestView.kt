package com.example.studydemo.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.studydemo.utils.px

//可以将任何单位转为px
fun dp2px(value: Float, view: View): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, view.resources.displayMetrics
    )
}

fun dp2px(value: Float, context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics
    )
}

fun dp2px(value: Float): Float {
    //Resources.getSystem()只能拿到和系统相关的上下文，如系统dpi的配置信息
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics
    )
}

class TestView(context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attributes, defStyleAttr) {

    private val RADIUS = 200f.px

    //抗锯齿效果打开
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(100f, 100f, 200f, 200f, paint)
        //整个View的宽和高
        val width = width.toFloat()
        val height = height.toFloat()
        canvas.drawCircle((width / 2), height / 2, RADIUS, paint)
        canvas.drawPath(path, paint)
    }
}