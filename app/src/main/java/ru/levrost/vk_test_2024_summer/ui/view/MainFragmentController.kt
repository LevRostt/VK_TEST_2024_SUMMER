package ru.levrost.vk_test_2024_summer.ui.view

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.ui.view.adapters.CategoryListRVAdapter
import ru.levrost.vk_test_2024_summer.ui.view.adapters.GridSpacingItemDecoration
import ru.levrost.vk_test_2024_summer.ui.view.adapters.LinearSpacingItemDecoration
import ru.levrost.vk_test_2024_summer.ui.view.adapters.ProductListRVAdapter
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel.QueryResult

class MainFragmentController(private val fragment: MainFragment, private val binding: FragmentMainBinding, private val viewModel: MainViewModel) {
    private val productListRVAdapter = ProductListRVAdapter(emptyList())
    private var isError = false
    fun setupRVs(){
        val spacing = fragment.resources.getDimensionPixelSize(R.dimen.rv_space)
        binding.productsRecyclerView.apply {
            layoutManager = GridLayoutManager(fragment.context, 2)
            adapter = productListRVAdapter
            addItemDecoration(GridSpacingItemDecoration(spacing))
            productListRVAdapter.changeRvListExtender(baseExtendRVList)
        }

        binding.categoriesRecyclerView.apply {
            val listiner = object : CategoryListRVAdapter.OnItemClickListener{
                override fun onItemClick(filter: String) {
                    if (viewModel.lastFilter == filter) {
                        setupBaseRV()
                        viewModel.lastFilter = ""
                    }
                    else {
                        viewModel.lastFilter = filter
                        setupFilterRV()
                    }
                }
            }
            layoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.HORIZONTAL, false)
            fragment.lifecycleScope.launch(Dispatchers.Main) {
                fragment.repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.category.collectLatest {
                        adapter = CategoryListRVAdapter(context, it, listiner)
                    }
                }
            }
            addItemDecoration(LinearSpacingItemDecoration(spacing))
        }



    }

    fun bindButtons(){
        binding.retryRequestBtn.setOnClickListener {
            viewModel.getLatestCategory()
            baseExtendRVList.extendRvList(0)
            binding.retryRequestBtn.isClickable = false
        }
    }

    private fun setupFilterRV(){
        productListRVAdapter.updateList(emptyList())
        productListRVAdapter.changeRvListExtender(filterExtendRVList)
    }

    private fun setupBaseRV(){
        productListRVAdapter.updateList(emptyList())
        productListRVAdapter.changeRvListExtender(baseExtendRVList)
    }

    interface RVListExtender{
        fun extendRvList(skip: Int)
    }

    private val baseExtendRVList = createRVListExtender { skip ->
        viewModel.getProductList(skip).collectLatest {
            handleQueryResult(it)
        }
    }

    private val filterExtendRVList = createRVListExtender { skip ->
        viewModel.getFilterProductList(skip).collectLatest {
            handleQueryResult(it)
        }
    }

    private fun createRVListExtender(block: suspend (Int) -> Unit): RVListExtender {
        return object : RVListExtender {
            override fun extendRvList(skip: Int) {
                binding.productsRecyclerView.clearOnScrollListeners()
                binding.productsRecyclerView.addOnScrollListener(
                    object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)
                            if (isError) {
                                extendRvList(skip)
                            }
                        }
                    }
                )
                isError = false
                fragment.lifecycleScope.launch(Dispatchers.Main) {
                    fragment.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        binding.rvLoading.visibility = View.GONE
                        block(skip)
                    }
                }
            }
        }
    }

    private fun handleQueryResult(result: QueryResult) {
        when (result) {
            is QueryResult.Success -> successResult(result.products)
            is QueryResult.Error -> showError(result.exception)
        }
    }


    private fun successResult(productsList: List<Product>){
        isError = false
        if(productsList.isNotEmpty()){
            binding.errorMess.visibility = View.GONE
            productListRVAdapter.extendList(productsList)
        }
    }

    private fun showError(e: Throwable){
        binding.retryRequestBtn.isClickable = true
        if (productListRVAdapter.itemCount == 0){
            binding.errorMess.visibility = View.VISIBLE
        }
        else{
            isError = true
//            Toast.makeText(fragment.context, fragment.resources.getText(R.string.server_wrong_mess_additional).toString(), Toast.LENGTH_SHORT).show()
        }
    }
}