package kspark.extension

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

inline fun <reified T: Any> String.fromJson(): T = gson.fromJson(this, T::class.java)

fun <T> T.toJson() : String {
    return gson.toJson(this)
}