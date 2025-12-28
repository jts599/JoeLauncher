@file:OptIn(InternalSerializationApi::class)
package app.joelauncher.helper

import app.joelauncher.data.Prefs
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class BackgroundSettings(
    val autoUpdateLockScreen: Boolean,
    val autoUpdateHomescreen: Boolean
)

/**
 * Checks if any background updates are enabled in the provided preferences.
 *
 * @param prefs The preferences object to check.
 */
fun anyBackgroundUpdateEnabled(prefs: Prefs): Boolean {
    val backgroundSettings = getCurrentBackgroundSettings(prefs)
    return backgroundSettings.autoUpdateLockScreen || backgroundSettings.autoUpdateHomescreen
}

/**
 * Disables all background updates in the provided preferences.
 *
 * @param prefs The preferences object in which to disable background updates.
 */
fun disableAllBackgroundUpdates(prefs: Prefs) {
    val newSettings = BackgroundSettings(
        autoUpdateLockScreen = false,
        autoUpdateHomescreen = false
    )
    setCurrentBackgroundSettings(newSettings, prefs)
}


/**
 * Gets the current background settings from the provided preferences.
 *
 * @param prefs The preferences object from which to retrieve the background settings.
 * @return A BackgroundSettings object representing the current background settings.
 */
fun getCurrentBackgroundSettings(prefs: Prefs): BackgroundSettings {
    //GetFromPrefs
    val serializedSettings = prefs.backgroundSettings
    return deserializeBackgroundSettings(serializedSettings)
}

/**
 * Sets the current background settings in the provided preferences.
 *
 * @param settings The BackgroundSettings object representing the new background settings.
 * @param prefs The preferences object in which to store the background settings.
 */
fun setCurrentBackgroundSettings(settings: BackgroundSettings, prefs: Prefs) {
    val serializedSettings = serializeBackgroundSettings(settings)
    prefs.backgroundSettings = serializedSettings
}


/**
 * Serializes a BackgroundSettings object into a JSON String.
 *
 * @param settings The BackgroundSettings object to convert.
 * @return A JSON string representation of the settings object.
 */
fun serializeBackgroundSettings(settings: BackgroundSettings): String {
    return Json.encodeToString(settings)
}

/**
 * Deserializes a JSON string back into a BackgroundSettings object.
 *
 * It will handle potential malformed strings by returning a default object.
 *
 * @param jsonString The JSON string to parse.
 * @return A BackgroundSettings object. Returns a default object with both values
 *         set to false if the string is null or cannot be parsed.
 */
fun deserializeBackgroundSettings(jsonString: String?): BackgroundSettings {
    return if (jsonString != null) {
        try {
            Json.decodeFromString<BackgroundSettings>(jsonString)
        } catch (_: Exception) {
            // In case of a parsing error (e.g., malformed string), return a safe default.
            BackgroundSettings(autoUpdateLockScreen = false, autoUpdateHomescreen = false)
        }
    } else {
        // If the string is null, return a default state.
        BackgroundSettings(autoUpdateLockScreen = false, autoUpdateHomescreen = false)
    }
}
