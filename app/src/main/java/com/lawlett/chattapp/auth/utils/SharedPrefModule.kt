package com.lawlett.chattapp.auth.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPrefModule(context: Context) {

    private var PRIVATE_MODE = 0
    private val KEY = "token"
    private val FIRST = "first"
    private val DEF_VALUE = "empty"
    private val sharedPref: SharedPreferences = context.getSharedPreferences(KEY, PRIVATE_MODE)

    inner class TokenModule {
        fun getToken(): String? {
            val temp = sharedPref.getString(KEY, DEF_VALUE)!!

            return if (temp == DEF_VALUE) {
                null
            } else {
                temp
            }
        }


        fun saveToken(token: String?) {
            val editor = sharedPref.edit()
            editor.putString(KEY, token)
            editor.apply()
        }

        fun deleteToken() {
            val editor = sharedPref.edit()
            editor.remove(KEY)
            editor.apply()
        }

    }

    inner class FirstTimeModule {

        fun isShown(): Boolean {
            return sharedPref.getBoolean(FIRST, false)
        }

        fun saveShown() {
            val editor = sharedPref.edit()
            editor.putBoolean(FIRST, true)
            editor.apply()
        }
    }
}