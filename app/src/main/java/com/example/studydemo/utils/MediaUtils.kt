package com.example.studydemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import com.example.studydemo.R

object MediaUtils {

    fun getAvatar(context: Context, width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher, options)
    }

    fun getColorRes(context: Context) {
        val blue = Color.BLUE
        val red = Color.argb(127, 255, 0, 0)
        val color = context.getColor(R.color.white)
        val color2 = context.resources.getColor(R.color.white)
    }
}