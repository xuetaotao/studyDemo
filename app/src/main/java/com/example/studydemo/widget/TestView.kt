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

    /**
     * View的尺寸发生变化的时候这个方法会调用
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()
        //Path.Direction.CW：clockwise，顺时针
        //Path.Direction.CCW: counter-clockwise，逆时针
        //它们的作用主要是在画多个图形时，它们相交的部分应该是填充还是空间。画单个图形的没啥作用。
        path.addCircle(width / 2f, height / 2f, RADIUS, Path.Direction.CCW)
        path.addRect(
            width / 2f - RADIUS,
            height / 2f,
            width / 2f + RADIUS,
            height / 2f + 2 * RADIUS,
            Path.Direction.CCW
        )
        path.addCircle(width / 2f, height / 2f, RADIUS * 1.5f, Path.Direction.CCW)
        //多个图形相交时，相交部分是否要被填充，是和Path.FillType，Path.Direction一起相关的
        //默认是WINDING，镂空主要用Path.FillType.EVEN_ODD，使用Path.FillType.WINDING的话还需要计算各种规则
        //INVERSE_EVEN_ODD就是EVEN_ODD的反规则
        path.fillType = Path.FillType.EVEN_ODD
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawLine(100f, 100f, 200f, 200f, paint)
        //整个View的宽和高
        val width = width.toFloat()
        val height = height.toFloat()
        canvas.drawCircle((width / 2), height / 2, RADIUS, paint)
        canvas.drawPath(path, paint)
    }
}