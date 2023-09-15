package com.example.studydemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.reflect.Field

//解决AppBarLayout抖动的问题: https://github.com/yuruiyin/AppbarLayoutBehavior
class AppBarLayoutBehavior(context: Context?, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {
    private var isFlinging = false
    private var shouldBlockNestedScroll = false
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
//        Tracker.d("onInterceptTouchEvent:" + child.totalScrollRange);
        shouldBlockNestedScroll = false
        if (isFlinging) {
            shouldBlockNestedScroll = true
        }
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> stopAppbarLayoutFling(child)
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }


    @get:Throws(NoSuchFieldException::class)
    private val flingRunnableField: Field
        get() = try {
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mFlingRunnable")
        } catch (e: NoSuchFieldException) {
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("flingRunnable")
        }


    @get:Throws(NoSuchFieldException::class)
    private val scrollerField: Field
        get() {
            return try {
                val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
                headerBehaviorType.getDeclaredField("mScroller")
            } catch (e: NoSuchFieldException) {
                val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
                headerBehaviorType.getDeclaredField("scroller")
            }
        }


    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        try {
            val flingRunnableField: Field = flingRunnableField
            val scrollerField: Field = scrollerField
            flingRunnableField.isAccessible = true
            scrollerField.isAccessible = true
            val flingRunnable: Runnable? = flingRunnableField.get(this) as Runnable?
            val overScroller: OverScroller? = scrollerField.get(this) as OverScroller?
            if (flingRunnable != null) {
//                Tracker.d("flingRunnable exist");
                appBarLayout.removeCallbacks(flingRunnable)
                flingRunnableField.set(this, null)
            }
            if (overScroller != null && !overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
//        Tracker.d("onStartNestedScroll")
        stopAppbarLayoutFling(child)
        return super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
//        Tracker.d("onNestedPreScroll:" + child.totalScrollRange + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type)
        if (type == TYPE_FLING) {
            isFlinging = true
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
//        Tracker.d("onNestedScroll: target:" + target.javaClass + " ," + child.totalScrollRange + " ,dxConsumed:" + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type)
        if (!shouldBlockNestedScroll) {
            //TODO
            super.onNestedScroll(
                coordinatorLayout,
                child,
                target,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed,
                type
            )
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
//        Tracker.d("onStopNestedScroll")
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
        isFlinging = false
        shouldBlockNestedScroll = false
    }

    companion object {
        private const val TYPE_FLING = 1
    }
}