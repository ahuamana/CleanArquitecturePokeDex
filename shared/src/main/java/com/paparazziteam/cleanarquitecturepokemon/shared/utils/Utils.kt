package com.paparazziteam.cleanarquitecturepokemon.shared.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.paparazziteam.cleanarquitecturepokemon.shared.R
import com.paparazziteam.cleanarquitecturepokemon.shared.databinding.CustomDialogCreateTeamBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
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

fun generateToken(): String {
    return UUID.randomUUID().toString().substring(0, 8)
}


fun Context.createTeamDialog(textDescription: String?,
                             @DrawableRes icon: Int = R.drawable.pokeapi,
                             @ColorRes color: Int = R.color.colorPrimary): Dialog {
    var customBinding = CustomDialogCreateTeamBinding.inflate(LayoutInflater.from(this))

    return Dialog(this).apply{
        setCancelable(true)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(customBinding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }.also { dialog->
        customBinding.tvDescription.text = textDescription?:""
        customBinding.ivIcon.setImageResource(icon)
        customBinding.btnOk.backgroundTintList = ContextCompat.getColorStateList(this, color)
        customBinding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableObject(key: String): T? {
    return if (this.containsKey(key)) this.get(key) as T else null
}

fun <T : Any> List<T?>.filterNotNullItems(): List<T> {
    return this.filterNotNull()
}

fun View.preventDoubleLongClick() {
    this.isEnabled = false
    this.postDelayed( { this.isEnabled = true }, 3000)
}

fun TextInputEditText.setMaxLength(maxLength: Int) {
    val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    this.filters = filters
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Copied token", text)
    clipboard.setPrimaryClip(clip)
}

fun View.setBackgrounColorWithAlpha(@ColorRes colorRes: Int, alpha: Float) {
    this.setBackgroundColor(ContextCompat.getColor(this.context, colorRes).withAlpha(alpha))
}

