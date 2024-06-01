package com.tastytrade.kurshin.presentation.ui.main.symbols

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.Symbol

class SymbolAdapter : RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>() {

    private var symbols: List<Symbol> = emptyList()

    class SymbolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.cbSymbolName)

        fun bind(symbol: Symbol) {
            nameTextView.text = symbol.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymbolViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_symbol, parent, false)
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
        symbols = newSymbols
        notifyDataSetChanged()
    }
}
