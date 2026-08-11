package com.andreikslpv.presentation_sets.recyclers

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.andreikslpv.domain_sets.entities.SetEntity
import com.andreikslpv.presentation_sets.databinding.ItemSetBinding
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class SetViewHolder(val binding: ItemSetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val imageLoader = ImageLoader.Builder(binding.root.context)
        .components { add(SvgDecoder.Factory()) }
        // Scryfall requires a non-generic User-Agent header for image requests.
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "MTGCollection/${getAppVersion(binding.root.context)} (android)")
                        .header("Accept", "image/*")
                        .build()
                    chain.proceed(request)
                }
                .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build()
        }
        .build()

    fun bind(set: SetEntity) {
        binding.itemTitle.text = set.name
        val request = ImageRequest.Builder(binding.root.context)
            .data(set.iconSvgUri)
            .target(binding.itemImage)
            .build()
        imageLoader.enqueue(request)
        binding.totalCount.text = set.cardCount.toString()
    }

    private fun getAppVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "1.0.0"
        } catch (_: Exception) {
            "1.0.0"
        }
    }

    private companion object {
        const val TIMEOUT_SECONDS = 30L
    }

}