package de.marmaro.krt.ffupdater.settings

import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatDelegate
import de.marmaro.krt.ffupdater.device.DeviceSdkTester

@Keep
object ForegroundSettings {
    private lateinit var preferences: SharedPreferences

    /**
     * This function must be called from Application.onCreate() or this singleton can't be used
     */
    fun init(sharedPreferences: SharedPreferences) {
        preferences = sharedPreferences
    }

    val isUpdateCheckOnMeteredAllowed
        get() = preferences.getBoolean("foreground__update_check__metered", true)

    val isDownloadOnMeteredAllowed
        get() = preferences.getBoolean("foreground__download__metered", true)

    private val validAndroidThemes = listOf(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_NO
    )

    val themePreference: Int
        get() {
            val theme = preferences.getString("foreground__theme_preference", null)?.toIntOrNull()
            return when {
                theme in validAndroidThemes -> theme!!
                // return default values because theme is invalid and could be null
                DeviceSdkTester.supportsAndroid10Q29() -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }

    val isDeleteUpdateIfInstallSuccessful
        get() = preferences.getBoolean("foreground__delete_cache_if_install_successful", true)

    val isDeleteUpdateIfInstallFailed
        get() = preferences.getBoolean("foreground__delete_cache_if_install_failed", true)

    val isHideWarningButtonForInstalledApps
        get() = preferences.getBoolean("foreground__hide_warning_button_for_installed_apps", false)

    val isHideAppsSignedByDifferentCertificate
        get() = preferences.getBoolean("foreground__hide_apps_signed_by_different_certificate", false)

}