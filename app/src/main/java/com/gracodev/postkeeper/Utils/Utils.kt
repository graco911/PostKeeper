package com.gracodev.postkeeper.Utils

import android.text.Editable
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.gracodev.postkeeper.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    return dateFormat.format(date)
}

fun String.truncate(limit: Int): String {
    return if (length > limit) {
        substring(0, limit) + "..."
    } else {
        this
    }
}

fun View.snackbarSuccess(message: String) {
    val snackbar = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG).also { _ ->
        }
    snackbar.view.background = ContextCompat.getDrawable(
        context,
        R.color.succes_color
    )
    snackbar.show()
}

fun View.snackbarError(message: String) {
    val snackbar = Snackbar
        .make(this, message, Snackbar.LENGTH_LONG).also { _ ->
        }
    snackbar.view.background = ContextCompat.getDrawable(
        context,
        R.color.error_color
    )
    snackbar.show()
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun AppCompatActivity.hideFabWithAnimation(fabId: Int, animationId: Int) {
    val fab = findViewById<FloatingActionButton>(fabId)
    val hideFabAnimation = AnimationUtils.loadAnimation(this, animationId)
    fab.startAnimation(hideFabAnimation)
    fab.visibility = View.INVISIBLE
}

fun AppCompatActivity.revealFabWithAnimation(fabId: Int, animationId: Int) {
    val fab = findViewById<FloatingActionButton>(fabId)
    val hideFabAnimation = AnimationUtils.loadAnimation(this, animationId)
    fab.startAnimation(hideFabAnimation)
    fab.visibility = View.VISIBLE
}