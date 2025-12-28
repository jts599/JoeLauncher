package app.joelauncher.helper

import app.joelauncher.data.Prefs
import java.time.Instant

fun allowAccessForNMins(n: Int, prefs: Prefs){
    val currentTime = System.currentTimeMillis()
    val expiryTime = currentTime + (n * 60 * 1000)
    prefs.appEnabledExpiry = Instant.ofEpochMilli(expiryTime)
}


fun isAccessCurrentlyEnabled(prefs: Prefs): Boolean {
    val expiryInstant = prefs.appEnabledExpiry ?: return false
    val now = Instant.now()
    return now.isBefore(expiryInstant)
}