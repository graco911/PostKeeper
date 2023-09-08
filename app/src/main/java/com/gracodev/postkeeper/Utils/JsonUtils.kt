package com.gracodev.postkeeper.Utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> T.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson<T>(this)
}

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)