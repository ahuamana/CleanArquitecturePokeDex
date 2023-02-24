package com.paparazziteam.cleanarquitecturepokemon.shared.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.paparazziteam.cleanarquitecturepokemon.shared.R
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

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


fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}

fun ShapeableImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.corner_shimmer_style)
        .error(R.drawable.corner_shimmer_style)
        .into(this)
}

fun String.toShortDescription(): String {
    return if (this.length > 20) {
        this.substring(0, 20) + "..."
    } else {
        this
    }
}

fun Int.withAlpha(alpha: Float): Int {
    val alphaInt = (255 * alpha).roundToInt()
    return Color.argb(alphaInt, Color.red(this), Color.green(this), Color.blue(this))
}

fun MaterialCardView?.setCardBackgroundColorWithAlpha(@ColorRes colorRes: Int, alpha: Float) {
    this?.setCardBackgroundColor(ContextCompat.getColor(this.context, colorRes).withAlpha(alpha))
}

fun MaterialCardView?.removeCardBackgroundColor() {
    this?.setCardBackgroundColor(Color.TRANSPARENT)
}

