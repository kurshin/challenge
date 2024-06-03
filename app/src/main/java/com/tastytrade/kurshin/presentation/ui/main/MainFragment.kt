package com.tastytrade.kurshin.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.databinding.FragmentMainBinding
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.presentation.ui.chart.ChartFragment
import com.tastytrade.kurshin.presentation.ui.main.symbols.SymbolAdapter
import com.tastytrade.kurshin.presentation.ui.main.watchlist.SelectWatchListDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    private val symbolAdapter: SymbolAdapter by lazy {
        SymbolAdapter(viewModel) { openChartFragment(it) }
    }

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

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        }

        setUpSearch()
        setUpWatchlistSelector()
        setUpSymbolAdapter()

        startPriceRefresh()
    }

    private fun setUpSearch() {
        binding.etSearch.addTextChangedListener {
            val isSymbolEntered = !it.isNullOrEmpty()
            if (isSymbolEntered) {
                viewModel.searchSymbol(it.toString())
                symbolAdapter.turnEditModeOn()
            } else {
                clearSearchSymbols()
            }
        }

        binding.btnDone.setOnClickListener {
            binding.etSearch.setText("")
            clearSearchSymbols()
            symbolAdapter.saveSelectedList()
            hideKeyboard()
        }
    }

    private fun clearSearchSymbols() {
        viewModel.symbolsForAutofill.postValue(emptyList())
    }

    private fun setUpWatchlistSelector() {
        viewModel.currentWatchlist.observe(viewLifecycleOwner) {
            binding.tvSelectedWatchList.text = it.name.ifEmpty { getString(R.string.all_symbols) }
            symbolAdapter.updateCurrentWatchList(it)
            updateEmptyDataMessage()
        }

        binding.clWatchListSelector.setOnClickListener {
            val selectWatchListDialog = SelectWatchListDialog(requireActivity(), viewModel)
            selectWatchListDialog.show()
        }
    }

    private fun setUpSymbolAdapter() {
        val recyclerView: RecyclerView = binding.recyclerViewSymbols
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = symbolAdapter

        viewModel.symbolsForAutofill.observe(viewLifecycleOwner) {
            if (binding.etSearch.text.isEmpty()) {
                symbolAdapter.setSymbols(emptyList())
            } else {
                symbolAdapter.setSymbols(it)
            }
            updateEmptyDataMessage()
        }
    }

    private fun updateEmptyDataMessage() {
        if (binding.etSearch.text.isEmpty()) {
            binding.tvEmptySybols.setText(R.string.start_typing_symbol)
        } else {
            binding.tvEmptySybols.setText(R.string.no_seacrh_results)
        }
        binding.tvEmptySybols.isVisible = symbolAdapter.itemCount == 0
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(requireActivity(), InputMethodManager::class.java)
        requireActivity().currentFocus?.let { view ->
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun openChartFragment(symbol: Symbol) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, ChartFragment.newInstance(symbol))
            .addToBackStack("chart")
            .commit()
    }

    private fun startPriceRefresh() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    fetchAndUpdatePrices()
                    delay(REFRESH_DELAY_MILLIS)
                }
            }
        }
    }

    private suspend fun fetchAndUpdatePrices() {
        val symbolList = symbolAdapter.getAllSymbolNames().toList()
        symbolList.forEach { symbol ->
            lifecycleScope.launch(Dispatchers.IO) {
                val quote = viewModel.fetchQuoteData(symbol)
                withContext(Dispatchers.Main) {
                    symbolAdapter.updateSymbol(quote)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val REFRESH_DELAY_MILLIS = 5000L
        fun newInstance() = MainFragment()
    }
}