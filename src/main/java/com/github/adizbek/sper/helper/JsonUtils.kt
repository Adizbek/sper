package com.github.adizbek.sper.helper

import com.google.gson.Gson

interface JSONConvertable {
    fun toJSON(): String = Gson().toJson(this)
}

inline fun <reified T> String.toObject(): T = Gson().fromJson(this, T::class.java)
