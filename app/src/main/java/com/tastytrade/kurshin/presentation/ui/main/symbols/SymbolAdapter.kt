package com.tastytrade.kurshin.presentation.ui.main.symbols

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel
import com.tastytrade.kurshin.presentation.ui.main.watchlist.showDeleteSymbolDialog

class SymbolAdapter(private val viewModel: MainViewModel, private val onSymbolSelected: (symbol: Symbol) -> Unit) :
    RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {

    private var symbols = mutableListOf<Symbol>()
    private var selectedSymbols = mutableListOf<Symbol>()
    private var isEditMode = false

    inner class SymbolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxSymbol = itemView.findViewById<CheckBox>(R.id.cbSymbolName)
        private val layoutSymbol = itemView.findViewById<ViewGroup>(R.id.layoutSymbol)
        private val symbolName = itemView.findViewById<TextView>(R.id.tvSymbolName)
        private val texLsatPrice = itemView.findViewById<TextView>(R.id.tvLastPrice)
        private val textAskPrice = itemView.findViewById<TextView>(R.id.tvAskPrice)
        private val textBidPrice = itemView.findViewById<TextView>(R.id.tvBidPrice)
        private val deleteSymbol = itemView.findViewById<ImageButton>(R.id.btnDeleteSymbol)

        fun bind(symbol: Symbol) {
            // isEditMode = true
            checkBoxSymbol.isVisible = isEditMode
            checkBoxSymbol.text = symbol.name
            checkBoxSymbol.isChecked = symbol.isChecked

            checkBoxSymbol.setOnClickListener {
                symbol.isChecked = (it as CheckBox).isChecked
                if (symbol.isChecked) {
                    if (!selectedSymbols.contains(symbol)) {
                        symbol.apply { watchListId = viewModel.currentWatchlist.value?.id ?: 0 }
                        selectedSymbols.add(symbol)
                        viewModel.addSymbol(symbol)
                    }
                } else {
                    selectedSymbols.remove(symbol)
                    viewModel.removeSymbol(symbol)
                }
            }
            // isEditMode = false
            layoutSymbol.isVisible = !isEditMode
            symbolName.text = symbol.name

            texLsatPrice.text = symbolName.context.getString(R.string.last_, String.format("%.2f", symbol.lastPrice))
            textAskPrice.text = symbolName.context.getString(R.string.ask_, String.format("%.2f", symbol.askPrice))
            textBidPrice.text = symbolName.context.getString(R.string.bid_, String.format("%.2f", symbol.bidPrice))

            layoutSymbol.setOnClickListener { onSymbolSelected.invoke(symbol) }

            deleteSymbol.setOnClickListener {
                it.context.showDeleteSymbolDialog(viewModel, symbol) {
                    val indexOf = symbols.indexOf(symbol)
                    symbols.remove(symbol)
                    notifyItemRemoved(indexOf)
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

    fun getAllSymbolNames() = selectedSymbols.map { it.name }

    @SuppressLint("NotifyDataSetChanged")
    fun setSymbols(symbolList: List<Symbol>) {
        val newSymbols = symbolList.map {
            val indexOf = selectedSymbols.indexOf(it)
            if (indexOf != -1) {
                it.apply {
                    isChecked = true
                    id = selectedSymbols[indexOf].id
                }
            } else {
                it.apply {
                    isChecked = false
                    id = 0
                }
            }
            it
        } + selectedSymbols.filterNot { symbolList.contains(it) }

        selectedSymbols.clear()
        selectedSymbols.addAll(newSymbols.filter { it.isChecked })

        symbols = newSymbols.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSymbols(symbolsToSet: List<Symbol>) {
        if (isEditMode) {
            val newSymbols = symbols.map {
                if (symbolsToSet.contains(it)) it.apply { isChecked = true } else it.apply { isChecked = false }
            } + symbolsToSet.filterNot { symbols.contains(it) }.map { it.apply { isChecked = false } }

            symbols = newSymbols.toMutableList()
            selectedSymbols = newSymbols.filter { it.isChecked }.toMutableList()
        } else {
            symbols = symbolsToSet.sortedBy { it.name }.toMutableList().onEach { it.isChecked = true }
            selectedSymbols = symbolsToSet.toMutableList()
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun saveSelectedList() {
        isEditMode = false
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun turnEditModeOn() {
        isEditMode = true
        notifyDataSetChanged()
    }

    fun updateSymbol(quote: Symbol) {
        val index = symbols.indexOfFirst { it.name == quote.name }
        if (index != -1) {
            symbols[index].lastPrice = quote.lastPrice
            symbols[index].askPrice = quote.askPrice
            symbols[index].bidPrice = quote.bidPrice
            notifyItemChanged(index)
        }
    }
}
