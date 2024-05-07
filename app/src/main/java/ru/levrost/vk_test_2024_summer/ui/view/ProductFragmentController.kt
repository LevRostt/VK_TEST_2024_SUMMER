package ru.levrost.vk_test_2024_summer.ui.view

import android.graphics.Paint
import androidx.viewpager2.widget.ViewPager2
import ru.levrost.vk_test_2024_summer.databinding.FragmentProductBinding
import ru.levrost.vk_test_2024_summer.ui.view.adapters.ViewPagerAdapter

class ProductFragmentController(private val productFragment: ProductFragment, private val binding: FragmentProductBinding) {

    fun viewPagerSetup(){
        val vpAdapter = ViewPagerAdapter(productFragment.product!!.images, productFragment.requireContext())
        binding.viewPager.adapter = vpAdapter
        binding.counter.text = "1/${productFragment.product!!.images.size}"
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.counter.text = "${position+1}/${productFragment.product!!.images.size}"
            }
        })
    }

    fun bindProductProperty(){
        productFragment.apply {
            binding.apply {
                productTitle.text = product!!.title
                productBrand.text = product!!.brand
                productDescription.text = product!!.description
                productPrice.text = product!!.price.toString()
                productRating.text = "рейтинг: ${product!!.rating}"
                productStock.text = "(${product!!.stock} шт.)"

                productWithoutDiscount.apply {
                    text = product!!.price.toString() + "$"
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                productPrice.text = "%.2f".format(product!!.price - product!!.price*(product!!.discountPercentage)/100) + "$"
//                productDiscount.text = "-${product!!.discountPercentage}%"


            }

        }

    }
}