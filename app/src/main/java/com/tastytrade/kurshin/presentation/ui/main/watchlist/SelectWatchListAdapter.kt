package com.tastytrade.kurshin.presentation.ui.main.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.DEFAULT_WATCHLIST
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

class SelectWatchListAdapter(
    private val viewModel: MainViewModel,
    private val allSymbolsSelector: RadioButton,
    private val dismissAction: () -> Unit
) : RecyclerView.Adapter<SelectWatchListAdapter.ViewHolder>() {

    private var selectedPosition = viewModel.watchList.indexOfFirst {
        it.name == viewModel.currentWatchlist.value?.name
    }
    private val items = viewModel.watchList

    init {
        allSymbolsSelector.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val previousPosition = selectedPosition
                selectedPosition = NOT_SELECTED
                notifyItemChanged(previousPosition)
                viewModel.currentWatchlist.postValue(DEFAULT_WATCHLIST)
                dismissAction.invoke()
            }
        }
        allSymbolsSelector.isChecked = selectedPosition == NOT_SELECTED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.watchlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position == selectedPosition)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxWatchList: RadioButton = itemView.findViewById(R.id.cbWatchlistItem)

        fun bind(watchList: WatchList, isSelected: Boolean) {
            checkBoxWatchList.text = watchList.name
            checkBoxWatchList.isChecked = isSelected

            checkBoxWatchList.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                allSymbolsSelector.isChecked = false
                viewModel.currentWatchlist.postValue(watchList)

                notifyItemChanged(previousPosition)
                notifyItemChanged(adapterPosition)

                dismissAction.invoke()
            }
        }
    }

    companion object {
        private const val NOT_SELECTED = -1
    }
}