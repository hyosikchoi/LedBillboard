package com.hyosik.presentation.extension

import android.content.Context
import android.widget.Toast
import com.hyosik.presentation.enum.ToastType

fun Context.toast(msg: String, type: ToastType) =
    if(type == ToastType.SHORT) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    else Toast.makeText(this, msg, Toast.LENGTH_LONG).show()