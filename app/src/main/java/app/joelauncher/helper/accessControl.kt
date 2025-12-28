package app.joelauncher.helper

import app.joelauncher.data.Prefs
import java.time.Instant

fun allowAccessForNMins(n: Int, prefs: Prefs){
    val currentTime = System.currentTimeMillis()
    val expiryTime = currentTime + (n * 60 * 1000)
    prefs.appEnabledExpiry = Instant.ofEpochMilli(expiryTime)
}

/**
 * Gets the expiry time of the access as a human readable string in local timezone
 */
fun getExiryTime(prefs: Prefs): String? {
    val expiryInstant = prefs.appEnabledExpiry ?: return ""
    val expiryDateTime = expiryInstant.atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
    val formatter = java.time.format.DateTimeFormatter.ofPattern("h:mm a")
    return expiryDateTime.format(formatter)
}


/**
 * Is App access currently on
 */
fun isAccessCurrentlyEnabled(prefs: Prefs): Boolean {
    val expiryInstant = prefs.appEnabledExpiry ?: return false
    val now = Instant.now()
    return now.isBefore(expiryInstant)
}

/**
 * Disable App Access
 */
fun disableAccess(prefs: Prefs) {
    prefs.appEnabledExpiry = null
}