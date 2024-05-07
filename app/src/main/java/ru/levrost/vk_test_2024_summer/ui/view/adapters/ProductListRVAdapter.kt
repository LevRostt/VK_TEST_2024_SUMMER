package ru.levrost.vk_test_2024_summer.ui.view.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentItemCardBinding
import ru.levrost.vk_test_2024_summer.debugLog

class ProductListRVAdapter(
    private val listiner: OnItemClickListener
) : RecyclerView.Adapter<ProductListRVAdapter.MainListHolder>(){
    private var rvListExtender: RVListExtender? = null
    private var productsList: List<Product> = emptyList()
    class MainListHolder(val binding: FragmentItemCardBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun onBind(product: Product){
            binding.apply {
                productTitle.text = product.title
                productDescription.text = product.description
                productThumbnail.load(product.thumbnail)
                productPrice.text = "%.2f".format(product.price - product.price*(product.discountPercentage)/100) + "$"

                productWithoutDiscount.apply {
                    text = product.price.toString() + "$"
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                productDiscount.text = "-${product.discountPercentage}%"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val binding = FragmentItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.onBind(productsList[position])
        holder.binding.allCard.setOnClickListener {
            listiner.onItemClick(productsList[position])
        }
        if (position==productsList.lastIndex) { //check position
            rvListExtender?.extendRvList(position+1)
        }
    }

    private fun updateList(list: List<Product>){
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

    interface OnItemClickListener {
        fun onItemClick(product: Product)
    }

    interface RVListExtender{
        fun extendRvList(skip: Int)
    }
}