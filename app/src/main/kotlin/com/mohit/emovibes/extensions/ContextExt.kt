/*
 * EMOVibes Project Original (2026)
 * Mohit (github.com/Mohit)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.mohit.emovibes.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mohit.emovibes.constants.InnerTubeCookieKey
import com.mohit.emovibes.constants.YtmSyncKey
import com.mohit.emovibes.utils.dataStore
import com.mohit.emovibes.utils.get
import com.mohit.emovibes.innertube.utils.parseCookieString

fun Context.isSyncEnabled(): Boolean {
    return dataStore.get(YtmSyncKey, true) && isUserLoggedIn()
}

fun Context.isUserLoggedIn(): Boolean {
    val cookie = dataStore[InnerTubeCookieKey] ?: ""
    return "SAPISID" in parseCookieString(cookie) && isInternetConnected()
}

fun Context.isInternetConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
}
