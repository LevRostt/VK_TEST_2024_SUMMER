package ru.levrost.vk_test_2024_summer.ui.view

import androidx.recyclerview.widget.GridLayoutManager
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel

class MainFragmentController(private val fragment: MainFragment, private val binding: FragmentMainBinding, private val viewModel: MainViewModel) {
    fun setupRV(){
        binding.recyclerViews.layoutManager = GridLayoutManager(fragment.context, 2)
        binding.recyclerViews.adapter = MainListRVAdapter(viewModel.getProductList())
    }
}