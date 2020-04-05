package de.marmaro.krt.ffupdater.download;

import android.util.Log;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import de.marmaro.krt.ffupdater.App;
import de.marmaro.krt.ffupdater.DeviceABI;

/**
 * Get the version name and the download link for the latest Fennec release (release, beta, nightly)
 * from the official Mozilla API.
 */
public class FennecVersionFinder {
    private static final String TAG = "ffupdater";
    private static final String DEFAULT_ABI = "android";
    private static final String X86_ABI = "android-x86";
    private static final String RELEASE_PRODUCT = "fennec-latest";
    private static final String BETA_PRODUCT = "fennec-beta-latest";
    private static final String NIGHTLY_PRODUCT = "fennec-nightly-latest";
    private static final String UTF_8 = "UTF-8";

    private static final String CHECK_URL = "https://product-details.mozilla.org/1.0/mobile_versions.json";
    private static final String DOWNLOAD_URL = "https://download.mozilla.org/?product=%s&os=%s&lang=multi";

    public static String getDownloadUrl(App app, DeviceABI.ABI abi) {
        String operatingSystem;
        switch (abi) {
            case AARCH64:
            case ARM:
                operatingSystem = DEFAULT_ABI;
                break;
            case X86:
            case X86_64:
                operatingSystem = X86_ABI;
                break;
            default:
                throw new IllegalArgumentException("unsupported abi " + abi);
        }

        String product;
        switch (app) {
            case FENNEC_RELEASE:
                product = RELEASE_PRODUCT;
                break;
            case FENNEC_BETA:
                product = BETA_PRODUCT;
                break;
            case FENNEC_NIGHTLY:
                product = NIGHTLY_PRODUCT;
                break;
            default:
                throw new IllegalArgumentException("unsupported app " + app);
        }
        return String.format(DOWNLOAD_URL, product, operatingSystem);
    }

    public static Optional<Version> getResponse() {
        Optional<String> json = downloadVersion();
        if (!json.isPresent()) {
            return Optional.absent();
        }
        Gson gson = new Gson();
        return Optional.of(gson.fromJson(json.get(), Version.class));
    }

    private static Optional<String> downloadVersion() {
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) new URL(CHECK_URL).openConnection();
            try (InputStream inputStream = urlConnection.getInputStream()) {
                return Optional.of(IOUtils.toString(inputStream, UTF_8));
            }
        } catch (IOException e) {
            Log.e(TAG, "cant getVersion latest firefox versions", e);
            return Optional.absent();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static class Version {
        @SerializedName("version")
        private String releaseVersion;

        @SerializedName("beta_version")
        private String betaVersion;

        @SerializedName("nightly_version")
        private String nightlyVersion;

        public String getReleaseVersion() {
            return releaseVersion;
        }

        public String getBetaVersion() {
            return betaVersion;
        }

        public String getNightlyVersion() {
            return nightlyVersion;
        }

        @Override
        public String toString() {
            return "Version{" +
                    "releaseVersion='" + releaseVersion + '\'' +
                    ", betaVersion='" + betaVersion + '\'' +
                    ", nightlyVersion='" + nightlyVersion + '\'' +
                    '}';
        }
    }
}