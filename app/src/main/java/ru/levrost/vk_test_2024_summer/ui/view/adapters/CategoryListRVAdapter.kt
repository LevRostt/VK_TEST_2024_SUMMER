package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.databinding.CategoryCardBinding
import java.util.Date

class CategoryListRVAdapter(
    private val context: Context,
    private val categoryList: List<String>,
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<CategoryListRVAdapter.MainListHolder>() {
    var activeFilterName : String? = ""
    private var lastPress: Long = 0

    private var activeHolder: MainListHolder? = null
    inner class MainListHolder(val binding: CategoryCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int){
            binding.apply {
                categotyName.text = categoryList[position]
                categotyName.setOnClickListener {
                    val press = Date().time
                    if (press - lastPress > 300) {
                        lastPress = press
                        listener.onItemClick(categoryList[position])
                        activatePosition(categoryList[position], this@MainListHolder)
                    }
                }
                if (activeFilterName == categoryList[position]){
                    activeHolder = this@MainListHolder
                    categotyName.background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                    categotyName.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                else {
                    categotyName.background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                    categotyName.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
        }
    }

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
        holder.onBind(position)
    }

    fun deActivatePosition(){
        activeFilterName = ""
        if (activeHolder != null) {
            activeHolder!!.binding.categotyName.apply {
                background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
        activeHolder = null
    }

    private fun activatePosition(position : String, holder: MainListHolder){
        when (activeFilterName){
            "" -> {
                activeFilterName = position
                activeHolder = holder
                activeHolder!!.binding.categotyName.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
            position -> {
                deActivatePosition()
            }
            else -> {
                if (activeHolder != null && holder != activeHolder){
                    activeHolder!!.binding.categotyName.apply {
                        background = ContextCompat.getDrawable(context, R.drawable.category_card_bg)
                        setTextColor(ContextCompat.getColor(context, R.color.black))
                    }
                }
                activeHolder = holder
                activeFilterName = position
                activeHolder!!.binding.categotyName.apply {
                    background = ContextCompat.getDrawable(context, R.drawable.active_category_card_bg)
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }
}