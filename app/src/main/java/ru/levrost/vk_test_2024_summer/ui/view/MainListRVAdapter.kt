package ru.levrost.vk_test_2024_summer.ui.view

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import okhttp3.internal.format
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentItemCardBinding
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel

class MainListRVAdapter(
    private var productsList: List<Product>,
    private val viewModel: MainViewModel,
    private var extendRVList: () -> Unit
) : RecyclerView.Adapter<MainListRVAdapter.MainListHolder>(){
    class MainListHolder(val binding: FragmentItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val binding = FragmentItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.binding.apply {
            productTitle.text = productsList[position].title
            productDescription.text = productsList[position].description
            productThumbnail.load(productsList[position].thumbnail)
            productPrice.text = productsList[position].price.toString() + "$"

            productWithoutDiscount.apply {
                text = "%.2f".format(productsList[position].price / (1 - productsList[position].discountPercentage / 100)) + "$"
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            productDiscount.text = "-${productsList[position].discountPercentage}%"
        }

        if ((position + 1)%20 == 0 && position==productsList.lastIndex) { //check position
            viewModel.nextPage(position + 1)
            extendRVList()
        }
    }

    fun updateList(list: List<Product>){
        notifyItemRangeRemoved(0,productsList.size)
        productsList = list
        notifyItemRangeInserted(productsList.size, list.size)
    }
    fun extendList(list: List<Product>){
        productsList += list
        notifyItemRangeInserted(productsList.size, list.size)
    }

    fun changeExtendRVList(func: () -> Unit){
        extendRVList = func
    }
}