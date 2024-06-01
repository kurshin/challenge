package com.tastytrade.kurshin.presentation.ui.main.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
    private var items: List<WatchList> = viewModel.watchList

    init {
        allSymbolsSelector.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = NOT_SELECTED
            notifyItemChanged(previousPosition)
            viewModel.currentWatchlist.postValue(DEFAULT_WATCHLIST)
            dismissAction.invoke()
        }

        allSymbolsSelector.isChecked = selectedPosition == NOT_SELECTED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_watchlist, parent, false)
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
        private val editButton: ImageButton = itemView.findViewById(R.id.btnEditList)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

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

            editButton.setOnClickListener {
                dismissAction.invoke()
                itemView.context.showEditWatchlistDialog(viewModel, watchList)
            }

            deleteButton.setOnClickListener {
                dismissAction.invoke()
                itemView.context.showDeleteWatchlistDialog(viewModel, watchList)
            }
        }
    }

    companion object {
        private const val NOT_SELECTED = -1
    }
}