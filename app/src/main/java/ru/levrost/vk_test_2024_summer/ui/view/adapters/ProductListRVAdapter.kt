package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentItemCardBinding
import ru.levrost.vk_test_2024_summer.debugLog
import ru.levrost.vk_test_2024_summer.ui.view.MainFragmentController.RVListExtender

class ProductListRVAdapter(
    private var productsList: List<Product>
) : RecyclerView.Adapter<ProductListRVAdapter.MainListHolder>(){
    private var rvListExtender: RVListExtender? = null
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
            productPrice.text = "%.2f".format(productsList[position].price - productsList[position].price*(productsList[position].discountPercentage)/100) + "$"

            productWithoutDiscount.apply {
                text = productsList[position].price.toString() + "$"
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }

            productDiscount.text = "-${productsList[position].discountPercentage}%"
        }

        if (position==productsList.lastIndex) { //check position
            rvListExtender?.extendRvList(position+1)
        }
    }

    fun updateList(list: List<Product>){
        notifyItemRangeRemoved(0,productsList.size)
        productsList = list
        notifyItemRangeInserted(productsList.size, list.size)
    }
    fun extendList(list: List<Product>){
        if(productsList.containsAll(list))
            updateList(list)
        else {
            productsList += list
            notifyItemRangeInserted(productsList.size, list.size)
        }
    }

    fun changeRvListExtender(extender: RVListExtender){
        updateList(emptyList())
        rvListExtender = extender
        extender.extendRvList(0)
    }
}