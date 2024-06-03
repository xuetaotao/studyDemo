package com.example.studydemo.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.widget.Toast

val Float.px
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

fun showToast(context: Context?, content: String? = null) {
    context?.applicationContext?.let {
        Toast.makeText(it, content, Toast.LENGTH_SHORT).show()
    }
}

fun showLog(content: String? = null) {
    Log.e("taotao-->", "$content")
}