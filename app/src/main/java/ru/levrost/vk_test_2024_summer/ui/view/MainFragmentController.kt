package ru.levrost.vk_test_2024_summer.ui.view

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.debugLog
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel.QueryResult

class MainFragmentController(private val fragment: MainFragment, private val binding: FragmentMainBinding, private val viewModel: MainViewModel) {
    private val rvAdapter = MainListRVAdapter(emptyList(), viewModel, ::baseExtendRVList)
    private var isError = false
    fun setupRV(){
        binding.recyclerViews.layoutManager = GridLayoutManager(fragment.context, 2)
        binding.recyclerViews.adapter = rvAdapter

        val spacing = fragment.resources.getDimensionPixelSize(R.dimen.rv_space)
        binding.recyclerViews.addItemDecoration(GridSpacingItemDecoration(spacing))

        baseExtendRVList()
    }

    private fun baseExtendRVList(){
        binding.recyclerViews.clearOnScrollListeners()

        binding.recyclerViews.addOnScrollListener(baseExtendListiner)

        isError = false
        binding.retryRequestBtn.setOnClickListener {
            baseExtendRVList()
            binding.retryRequestBtn.isClickable = false
        }
        fragment.apply{
            lifecycleScope.launch(Dispatchers.Main) {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    binding.rvLoading.visibility = View.GONE
                    viewModel.getProductList().collectLatest {
                        when(it){
                            is QueryResult.Success -> successResult(it.products)
                            is QueryResult.Error -> showError(it.exception)
                        }
                    }
                }
            }
        }
    }

    private val baseExtendListiner = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(isError){
                viewModel.nextPage(rvAdapter.itemCount)
                baseExtendRVList()
            }
        }
    }

    private fun successResult(productsList: List<Product>){
        isError = false
        if(productsList.isNotEmpty()){
            binding.errorMess.visibility = View.GONE
            rvAdapter.extendList(productsList)
        }
    }

    private fun showError(e: Throwable){
        binding.retryRequestBtn.isClickable = true
        if (rvAdapter.itemCount == 0){
            binding.errorMess.visibility = View.VISIBLE
        }
        else{
            isError = true
//            Toast.makeText(fragment.context, fragment.resources.getText(R.string.server_wrong_mess_additional).toString(), Toast.LENGTH_SHORT).show()
        }
    }
}