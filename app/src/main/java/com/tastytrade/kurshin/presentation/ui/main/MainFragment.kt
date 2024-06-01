package com.tastytrade.kurshin.presentation.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.databinding.FragmentMainBinding
import com.tastytrade.kurshin.presentation.ui.main.symbols.SymbolAdapter
import com.tastytrade.kurshin.presentation.ui.main.watchlist.SelectWatchListDialog

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(requireContext())
    }
    private val symbolAdapter = SymbolAdapter()

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
        setUpQuotesUpdate()
    }

    private fun setUpSearch() {
        binding.etSearch.addTextChangedListener {
            val isSymbolEntered = !it.isNullOrEmpty()
            binding.btnDone.isEnabled = isSymbolEntered
            if (isSymbolEntered) {
                viewModel.searchSymbol(it.toString())
            } else {
                clearSearchSymbols()
            }
        }

        binding.btnDone.setOnClickListener {
            binding.etSearch.setText("")
            clearSearchSymbols()
        }
    }

    private fun clearSearchSymbols() {
        viewModel.symbols.postValue(emptyList())
    }

    private fun setUpWatchlistSelector() {
        viewModel.currentWatchlist.observe(viewLifecycleOwner) {
            binding.tvSelectedWatchList.text = it.name.ifEmpty { getString(R.string.all_symbols) }
        }

        binding.clWatchListSelector.setOnClickListener{
            val selectWatchListDialog = SelectWatchListDialog(requireActivity(), viewModel)
            selectWatchListDialog.show()
        }
    }

    private fun setUpSymbolAdapter() {
        val emptyAdapterView = requireActivity().findViewById<TextView>(R.id.tvEmptySybols)
        val recyclerView: RecyclerView = requireActivity().findViewById(R.id.recyclerViewSymbols)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = symbolAdapter

        viewModel.symbols.observe(viewLifecycleOwner) {
            symbolAdapter.setSymbols(it)
            emptyAdapterView.isVisible = it.isEmpty()
        }
    }

    private fun setUpQuotesUpdate() {
        viewModel.quote.observe(viewLifecycleOwner) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}