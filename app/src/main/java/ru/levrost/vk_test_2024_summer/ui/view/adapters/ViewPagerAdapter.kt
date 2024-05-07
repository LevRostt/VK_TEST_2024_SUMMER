package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.debugLog

class ViewPagerAdapter(private val images: List<String>, private val context: Context) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {
    class PagerViewHolder(val layout: FrameLayout) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        return PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent,false) as FrameLayout)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.layout.findViewById<ImageView>(R.id.image_view).apply {
            val request = ImageRequest.Builder(context)
                .data(images[position])
                .error(R.drawable.image_download_error)
                .target(this)
                .build()
            Coil.imageLoader(context).enqueue(request)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }

}