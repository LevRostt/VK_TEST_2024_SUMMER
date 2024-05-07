package ru.levrost.vk_test_2024_summer.ui.view

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.levrost.vk_test_2024_summer.R
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.debugLog
import ru.levrost.vk_test_2024_summer.ui.view.adapters.CategoryListRVAdapter
import ru.levrost.vk_test_2024_summer.ui.view.adapters.GridSpacingItemDecoration
import ru.levrost.vk_test_2024_summer.ui.view.adapters.LinearSpacingItemDecoration
import ru.levrost.vk_test_2024_summer.ui.view.adapters.ProductListRVAdapter
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel.QueryResult
import java.util.Date

class MainFragmentController(private val fragment: MainFragment, private val binding: FragmentMainBinding, private val viewModel: MainViewModel) {
    private var productListRVAdapter: ProductListRVAdapter? = null
    private var categoryRVAdapter: CategoryListRVAdapter? = null
    private var isError = false
    fun setupRVs(){
        val spacing = fragment.resources.getDimensionPixelSize(R.dimen.rv_space)
        binding.productsRecyclerView.apply {
            val listiner = object : ProductListRVAdapter.OnItemClickListener{
                override fun onItemClick(product: Product) {
                    showProductFragment(product)
                }
            }
            productListRVAdapter = ProductListRVAdapter(listiner)
            adapter = productListRVAdapter
            layoutManager = GridLayoutManager(fragment.context, 2)
            addItemDecoration(GridSpacingItemDecoration(spacing))
            if (viewModel.currentFilter != "")
                setupFilterRV()
            else
                setupBaseRV()
        }

        binding.categoriesRecyclerView.apply {
            val listiner = object : CategoryListRVAdapter.OnItemClickListener{
                override fun onItemClick(filter: String) {
                    if (viewModel.currentFilter == filter) {
                        setupBaseRV()
                        viewModel.currentFilter = ""
                    }
                    else {
                        viewModel.currentFilter = filter
                        setupFilterRV()
                    }
                }
            }
            layoutManager = LinearLayoutManager(fragment.context, LinearLayoutManager.HORIZONTAL, false)
            fragment.lifecycleScope.launch(Dispatchers.Main) {
                fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                    viewModel.category.collectLatest {
                        categoryRVAdapter = CategoryListRVAdapter(context, it, listiner)
                        categoryRVAdapter!!.activeFilterName = viewModel.currentFilter
                        adapter = categoryRVAdapter
                    }
                }
            }
            addItemDecoration(LinearSpacingItemDecoration(spacing))
        }

    }

    fun setupStartupButton(){
        binding.retryRequestBtn.setOnClickListener {
            viewModel.getLatestCategory()
            baseExtendRVList.extendRvList(0)
            binding.retryRequestBtn.isClickable = false
        }
    }

    fun setupSearchView(){
        if (viewModel.currentQuery != ""){
            binding.searchView.setQuery(viewModel.currentQuery, false)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.currentQuery = query ?: ""
                setupSearchRV()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return if (newText == "") {
                    viewModel.currentQuery = newText
                    setupNoQueryRV()
                    true
                } else {
                    categoryRVAdapter?.deActivatePosition()
                    viewModel.currentFilter = ""
                    false
                }
            }

        })
    }

    private fun showProductFragment(product: Product){
        fragment.parentFragmentManager.beginTransaction()
            .add(R.id.top_container, ProductFragment(product))
            .addToBackStack("product")
            .commit()
    }

    private fun setupNoQueryRV(){
        if (viewModel.currentFilter == "")
            setupBaseRV()
        else
            setupFilterRV()
    }

    private fun setupBaseRV(){
        productListRVAdapter!!.changeRvListExtender(baseExtendRVList)

        binding.retryRequestBtn.setOnClickListener {
            viewModel.getLatestCategory()
            baseExtendRVList.extendRvList(0)
            binding.retryRequestBtn.isClickable = false
        }
    }

    private fun setupFilterRV(){
        productListRVAdapter!!.changeRvListExtender(filterExtendRVList)

        binding.retryRequestBtn.setOnClickListener {
            filterExtendRVList.extendRvList(0)
            binding.retryRequestBtn.isClickable = false
        }
    }

    private fun setupSearchRV(){
        productListRVAdapter!!.changeRvListExtender(searchExtendRVList)

        binding.retryRequestBtn.setOnClickListener {
            searchExtendRVList.extendRvList(0)
            binding.retryRequestBtn.isClickable = false
        }
    }

    private val baseExtendRVList = createRVListExtender { skip ->
        viewModel.getProducts(skip).collectLatest {
            handleQueryResult(it)
        }
    }

    private val filterExtendRVList = createRVListExtender { skip ->
        viewModel.getFilterProducts(skip).collectLatest {
            handleQueryResult(it)
        }
    }

    private val searchExtendRVList = createRVListExtender { skip ->
        viewModel.getSearchedProducts(skip).collectLatest {
            handleQueryResult(it)
        }
    }

    private fun createRVListExtender(block: suspend (Int) -> Unit): ProductListRVAdapter.RVListExtender {
        return object : ProductListRVAdapter.RVListExtender {
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
                    fragment.viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
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
            productListRVAdapter!!.extendList(productsList)
        }
    }

    private fun showError(e: Throwable){
        binding.retryRequestBtn.isClickable = true
        if (productListRVAdapter!!.itemCount == 0){
            binding.errorMess.visibility = View.VISIBLE
        }
        else{
            isError = true
        }
        showErrToast()
    }

    private var lastShow: Long = 0
    private fun showErrToast(){
        if (Date().time - lastShow > 2000) {
            lastShow = Date().time
            Toast.makeText(
                fragment.context,
                fragment.resources.getText(R.string.server_wrong_mess_additional).toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}