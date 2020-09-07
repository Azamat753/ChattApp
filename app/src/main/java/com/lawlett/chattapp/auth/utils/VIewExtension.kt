package com.lawlett.chattapp.auth.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast =
    Toast.makeText(this, message, duration).apply {
        show()


    }
fun View.visible() {
    visibility = View.VISIBLE
}
fun hasEmptyFields(vararg list: EditText): Boolean {
    list.forEach { if (it.text.toString().isEmpty())
        return false }
    return true
}
fun View.gone() {
    visibility = View.GONE
}