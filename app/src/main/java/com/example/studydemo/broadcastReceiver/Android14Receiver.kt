package com.example.studydemo.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.studydemo.utils.showToast

class Android14Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        showToast(context, "Hi, this is Android14 BroadcastReceiver")
    }
}