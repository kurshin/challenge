package com.tastytrade.kurshin.presentation.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tastytrade.kurshin.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels { ViewModelFactory }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.chart.observe(viewLifecycleOwner) {
            Log.i("1111", "chart = ${it}")
        }

        viewModel.quote.observe(viewLifecycleOwner) {
            Log.i("1111", "quote = ${it}")
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Log.i("1111", "error = $it")
        }

        viewModel.getChartData("aapl")
        viewModel.getSymbolData("aapl")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}