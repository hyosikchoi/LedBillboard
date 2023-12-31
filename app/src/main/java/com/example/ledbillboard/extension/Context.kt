package com.example.ledbillboard.extension

import android.content.Context
import android.widget.Toast
import com.example.ledbillboard.enum.ToastType

fun Context.toast(msg: String, type: ToastType) =
    if(type == ToastType.SHORT) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    else Toast.makeText(this, msg, Toast.LENGTH_LONG).show()