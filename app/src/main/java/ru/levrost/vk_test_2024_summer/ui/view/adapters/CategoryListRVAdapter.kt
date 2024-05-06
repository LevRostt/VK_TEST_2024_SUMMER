package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.databinding.CategoryCardBinding
import ru.levrost.vk_test_2024_summer.debugLog
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel

class CategoryListRVAdapter(
    private val context: Context,
    private val categoryList: List<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CategoryListRVAdapter.MainListHolder>() {
    class MainListHolder(val binding: CategoryCardBinding) : RecyclerView.ViewHolder(binding.root)
    private var activePosition : Int? = null
    private var activeHolder: MainListHolder? = null
    interface OnItemClickListener {
        fun onItemClick(filter: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val binding =
            CategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.binding.apply {
            categotyName.text = categoryList[position]
            categotyName.setOnClickListener {
                listener.onItemClick(categoryList[position])
                activatePosition(position, holder)
            }
            if (activePosition == position){
                activeHolder = holder
                categotyName.background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                categotyName.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            else {
                categotyName.background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                categotyName.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    private fun activatePosition(position : Int, holder: MainListHolder){
        when (activePosition){
            null -> {
                activePosition = position
                activeHolder = holder
                activeHolder!!.binding.categotyName.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            position -> {
                holder.binding.categotyName.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                activePosition = null
                activeHolder = null
            }
            else -> {
                if (holder != activeHolder){
                    debugLog("else")
                    activeHolder!!.binding.categotyName.apply {
                        background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                    }
                }
                activeHolder = holder
                activePosition = position
                activeHolder!!.binding.categotyName.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }
}