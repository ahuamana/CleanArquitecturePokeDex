package com.paparazziteam.cleanarquitecturepokemon.shared.utils

import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> toJson(value : T) = Json{
    encodeDefaults = true
    ignoreUnknownKeys = true
    isLenient = true
}.encodeToString(value)



inline fun <reified T> fromJson(json: String) : T {
    return Json{
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(json)
}

fun setBackgroundResourceWithTint(view: View, @DrawableRes drawableRes: Int, @ColorRes colorRes: Int) {
    val drawable = ContextCompat.getDrawable(view.context, drawableRes)
    drawable?.setColorFilter(ContextCompat.getColor(view.context, colorRes), PorterDuff.Mode.SRC_ATOP)
    view.background = drawable
}