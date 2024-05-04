package ru.levrost.vk_test_2024_summer.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentItemCardBinding

class MainListRVAdapter(private var list: List<Product>) : RecyclerView.Adapter<MainListRVAdapter.MainListHolder>(){

    class MainListHolder(val binding: FragmentItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val binding = FragmentItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainListHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.binding.productTitle.text = list[position].title
        holder.binding.productDescription.text = list[position].description
        holder.binding.productImage.load(list[position].thumbnail)
    }

    fun updateList(list: List<Product>){
        this.list = list
        notifyDataSetChanged()
    }
}