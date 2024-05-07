package ru.levrost.vk_test_2024_summer.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.levrost.vk_test_2024_summer.databinding.FragmentMainBinding
import ru.levrost.vk_test_2024_summer.ui.viewModel.MainViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var _controller: MainFragmentController? = null
    private val viewModel: MainViewModel by viewModels()
    private val controller get() = _controller!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        _controller = MainFragmentController(this, binding, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.setupRVs()
        controller.setupSearchView()
        controller.startSetupButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _controller = null
    }
}