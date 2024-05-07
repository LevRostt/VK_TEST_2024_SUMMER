package ru.levrost.vk_test_2024_summer.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.levrost.vk_test_2024_summer.data.model.Product
import ru.levrost.vk_test_2024_summer.databinding.FragmentProductBinding
import ru.levrost.vk_test_2024_summer.ui.viewModel.ProductViewModel

class ProductFragment(): Fragment() {
    private var _binding : FragmentProductBinding? = null
    private val binding get() = _binding!!
    private var _controller: ProductFragmentController? = null
    private val controller get() = _controller!!
    private val viewModel: ProductViewModel by viewModels()
    var product: Product? = null

    constructor(product: Product) : this(){
        this.product = product
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        _controller = ProductFragmentController(this, binding)

        if (viewModel.product == null)
            viewModel.product = product
        else
            product = viewModel.product

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.viewPagerSetup()
        controller.bindProductProperty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _controller = null
    }
}