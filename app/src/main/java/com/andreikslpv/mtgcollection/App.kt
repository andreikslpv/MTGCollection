package com.andreikslpv.mtgcollection

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.andreikslpv.common.Core
import com.andreikslpv.common.CoreProvider
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), ImageLoaderFactory {

    @Inject
    lateinit var coreProvider: CoreProvider

    override fun onCreate() {
        super.onCreate()
        Core.init(coreProvider)
    }

    override fun newImageLoader(): ImageLoader {
        val okHttpClient = OkHttpClient.Builder()
            // Scryfall requires a non-generic User-Agent header for image requests.
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "MTGCollection/$appVersion (android)")
                    .header("Accept", "image/*")
                    .build()
                chain.proceed(request)
            }
            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        return ImageLoader.Builder(this)
            .okHttpClient { okHttpClient }
            .build()
    }

    private val appVersion: String
        get() = try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0.0"
        } catch (_: Exception) {
            "1.0.0"
        }

    private companion object {
        const val TIMEOUT_SECONDS = 30L
    }

}