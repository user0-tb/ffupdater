package de.marmaro.krt.ffupdater.app.impl

import android.content.Context
import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.MainThread
import de.marmaro.krt.ffupdater.R
import de.marmaro.krt.ffupdater.app.App
import de.marmaro.krt.ffupdater.app.entity.DisplayCategory.BETTER_THAN_GOOGLE_CHROME
import de.marmaro.krt.ffupdater.app.entity.LatestVersion
import de.marmaro.krt.ffupdater.device.DeviceAbiExtractor
import de.marmaro.krt.ffupdater.network.exceptions.NetworkException
import de.marmaro.krt.ffupdater.network.fdroid.CustomRepositoryConsumer
import de.marmaro.krt.ffupdater.network.file.CacheBehaviour
import de.marmaro.krt.ffupdater.settings.DeviceSettingsHelper

/**
 * https://github.com/Divested-Mobile/mull-fenix
 */
@Keep
object MulchSystemWebView : AppBase() {
    override val app = App.MULCH_SYSTEMWEBVIEW
    override val packageName = "us.spotco.mulch_wv"
    override val title = R.string.mulch_wv__title
    override val description = R.string.mulch_wv__description
    override val installationWarning = R.string.mulch__warning
    override val downloadSource = "https://divestos.org/fdroid/official"
    override val icon = R.drawable.ic_logo_mulch
    override val minApiLevel = Build.VERSION_CODES.N
    override val supportedAbis = ARM32_ARM64
    override val installableByUser = false

    @Suppress("SpellCheckingInspection")
    override val signatureHash = "260e0a49678c78b70c02d6537add3b6dc0a17171bbde8ce75fd4026a8a3e18d2"
    override val projectPage = "https://divestos.org/fdroid/official/"
    override val displayCategory = listOf(BETTER_THAN_GOOGLE_CHROME)

    @MainThread
    @Throws(NetworkException::class)
    override suspend fun fetchLatestUpdate(context: Context, cacheBehaviour: CacheBehaviour): LatestVersion {
        val abi = DeviceAbiExtractor.findBestAbi(supportedAbis, DeviceSettingsHelper.prefer32BitApks)
        return CustomRepositoryConsumer.getLatestUpdate(
            "https://divestos.org/fdroid/official",
            packageName,
            abi,
            cacheBehaviour
        )
    }
}