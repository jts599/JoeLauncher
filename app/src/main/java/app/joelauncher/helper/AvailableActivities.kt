package app.joelauncher.helper

import android.content.Context
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.os.UserManager
import app.joelauncher.BuildConfig
import app.joelauncher.data.AppModel
import app.joelauncher.data.Constants
import app.joelauncher.data.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Collator


data class AppSearchSettings(
    /**
     * Include hidden apps in the search results
     */
    val includeHidden: Boolean = false,
    /**
     * Include regular apps in the search results
     */
    val includeRegular: Boolean = true,
    /**
     * Should the app filter be applied
     */
    val appFilterEnabled: Boolean = false,
)


/**
 * Backwards Compatibility wrapper for getAppsListCore
 */
@Deprecated("Use getAppsListCore instead")
suspend fun getAppsList(
    context: Context,
    prefs: Prefs,
    includeRegularApps: Boolean = true,
    includeHiddenApps: Boolean = false,
): MutableList<AppModel> {
    val appSearchSettings = AppSearchSettings(
        includeHidden = includeHiddenApps,
        includeRegular = includeRegularApps,
        appFilterEnabled = isAccessCurrentlyEnabled(prefs)
    )
    return getAppsListCore(context,prefs,appSearchSettings)
}

/**
 * Gets a list of apps to display in a app list
 * @param context System user context
 * @param prefs User preferences
 * @param searchSettings App search settings
 * @return
 */
suspend fun getAppsListCore(
    context: Context,
    prefs: Prefs,
    searchSettings: AppSearchSettings
): MutableList<AppModel> {
    return withContext(Dispatchers.IO) {
        val appList = mutableListOf<AppModel>()

        try {
            if (!prefs.hiddenAppsUpdated) upgradeHiddenApps(prefs)
            val hiddenApps = prefs.hiddenApps

            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
            val collator = Collator.getInstance()

            for (profile in userManager.userProfiles) {
                val activities = launcherApps.getActivityList(null, profile)
                for (activity in activities) {
                    val pkg = activity.applicationInfo.packageName

                    if (!isActivityIncluded(pkg, profile, hiddenApps, searchSettings)) continue

                    val appLabelShown = prefs.getAppRenameLabel(pkg).ifBlank { activity.label.toString() }
                    val appModel = AppModel(
                        appLabelShown,
                        collator.getCollationKey(activity.label.toString()),
                        pkg,
                        activity.componentName.className,
                        (System.currentTimeMillis() - activity.firstInstallTime) < Constants.ONE_HOUR_IN_MILLIS,
                        profile
                    )

                    appList.add(appModel)
                }
            }

            appList.sortBy { it.appLabel.lowercase() }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        appList
    }
}

private fun isActivityIncluded(
    packageName: String,
    profile: UserHandle,
    hiddenApps: Set<String>,
    searchSettings: AppSearchSettings
): Boolean {
    if (packageName == BuildConfig.APPLICATION_ID) return false
    if (searchSettings.appFilterEnabled) return false


    val key = "$packageName|$profile"
    val isHidden = hiddenApps.contains(key)

    return when {
        isHidden -> searchSettings.includeHidden
        else -> searchSettings.includeRegular
    }
}

// This is to ensure backward compatibility with older app versions
// which did not support multiple user profiles
private fun upgradeHiddenApps(prefs: Prefs) {
    val hiddenAppsSet = prefs.hiddenApps
    val newHiddenAppsSet = mutableSetOf<String>()
    for (hiddenPackage in hiddenAppsSet) {
        if (hiddenPackage.contains("|")) newHiddenAppsSet.add(hiddenPackage)
        else newHiddenAppsSet.add(hiddenPackage + android.os.Process.myUserHandle().toString())
    }
    prefs.hiddenApps = newHiddenAppsSet
    prefs.hiddenAppsUpdated = true
}