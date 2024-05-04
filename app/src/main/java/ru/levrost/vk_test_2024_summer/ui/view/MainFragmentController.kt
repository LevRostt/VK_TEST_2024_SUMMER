package ru.levrost.vk_test_2024_summer.ui.view

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel.QueryResult

class MainFragmentController(private val fragment: MainFragment, private val binding: FragmentMainBinding, private val viewModel: MainViewModel) {
    private val rvAdapter = MainListRVAdapter(emptyList())
    fun setupRV(){
        binding.recyclerViews.layoutManager = GridLayoutManager(fragment.context, 2)
        binding.recyclerViews.adapter = rvAdapter

        fragment.apply{
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.getProductList().collect {
                        when(it){
                            is QueryResult.Success -> rvAdapter.updateList(it.products)
                            is QueryResult.Error -> showError(it.exception)
                        }
                    }
                }
            }
        }

    }

    private fun showError(e: Throwable){
        Log.d("DebugMess", e.message.toString())
    }
}