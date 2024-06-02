package com.tastytrade.kurshin.presentation.ui.chart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tastytrade.kurshin.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    private val viewModel: ChartViewModel by viewModels { ViewModelFactory }
    private lateinit var symbolName: String

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbolName = it.getString(ARG_SYMBOL, "")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.chart.observe(viewLifecycleOwner) {
        }

        viewModel.getChartData(symbolName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_SYMBOL = "arg_symbol"
        fun newInstance(symbol: String): ChartFragment {
            val fragment = ChartFragment()
            val args = Bundle()
            args.putString(ARG_SYMBOL, symbol)
            fragment.arguments = args
            return fragment
        }
    }
}