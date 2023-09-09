package com.gracodev.postkeeper.Utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
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

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

fun Boolean.toVisibility() = if (this) View.GONE else View.VISIBLE

fun String.formatToCustomDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.US)

    try {
        val date = inputFormat.parse(this)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this
}

fun Context.openURLWithBrowser(url: String) {
    ContextCompat.startActivity(
        this,
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        ), Bundle.EMPTY
    )
}