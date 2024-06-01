package com.tastytrade.kurshin.presentation.ui.main.symbols

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.DEFAULT_WATCHLIST
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

class SymbolAdapter(private val viewModel: MainViewModel) :
    RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {

    private var symbols = emptyList<Symbol>()
    private var selectedSymbols = mutableListOf<Symbol>()
    private var lastSearchSymbols = emptyList<Symbol>()
    private var currentWatchList: WatchList = DEFAULT_WATCHLIST

    fun updateCurrentWatchList(watchlist: WatchList) {
        currentWatchList = watchlist
        selectedSymbols = if (currentWatchList == DEFAULT_WATCHLIST) {
            viewModel.selectedSymbols.distinct().toMutableList()
        } else {
            viewModel.selectedSymbols.filter { it.watchList == currentWatchList }.toMutableList()
        }.onEach { it.isChecked = true }
        setSymbols(lastSearchSymbols.onEach { it.isChecked = false })
    }

    inner class SymbolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxSymbol = itemView.findViewById<CheckBox>(R.id.cbSymbolName)

        fun bind(symbol: Symbol) {
            checkBoxSymbol.text = symbol.name
            checkBoxSymbol.isChecked = symbol.isChecked

            checkBoxSymbol.setOnClickListener {
                symbol.isChecked = (it as CheckBox).isChecked
                if (symbol.isChecked) {
                    if (!selectedSymbols.contains(symbol)) {
                        symbol.apply { watchList = currentWatchList }
                        selectedSymbols.add(symbol)
                        viewModel.selectedSymbols.add(symbol)
                    }
                } else {
                    selectedSymbols.remove(symbol)
                    viewModel.selectedSymbols.remove(symbol)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_symbol, parent, false)
        return SymbolViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SymbolViewHolder, position: Int) {
        holder.bind(symbols[position])
    }

    override fun getItemCount(): Int {
        return symbols.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSymbols(newSymbols: List<Symbol>) {
        symbols = newSymbols.map {
            if (selectedSymbols.contains(it)) it.apply { isChecked = true } else it
        } + selectedSymbols.filterNot { newSymbols.contains(it) }

        selectedSymbols.clear()
        selectedSymbols.addAll(symbols.filter { it.isChecked })
        notifyDataSetChanged()

        lastSearchSymbols = newSymbols
    }
}
