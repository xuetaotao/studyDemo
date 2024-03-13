package com.example.studydemo.base

import android.view.View

fun View.setOnSingleClickListener(block: ((v: View) -> Unit)?) {
    if (block == null) {
        setOnClickListener(null)
    } else {
        setOnClickListener(object : OnSingleClickListener() {
            override fun onClick() {
                block(this@setOnSingleClickListener)
            }
        })
    }
}

abstract class OnSingleClickListener : View.OnClickListener {
    private val clickInterval = 400L
    private var lastClickTimestamp: Long = 0

    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now - lastClickTimestamp > clickInterval) {
            lastClickTimestamp = now
            onClick()
        }
    }

    abstract fun onClick()
}