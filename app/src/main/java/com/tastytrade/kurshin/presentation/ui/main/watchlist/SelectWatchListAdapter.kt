package com.tastytrade.kurshin.presentation.ui.main.watchlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

class SelectWatchListAdapter(
    private val viewModel: MainViewModel,
    private val dismissAction: () -> Unit
) : RecyclerView.Adapter<SelectWatchListAdapter.ViewHolder>() {

    private var selectedPosition = viewModel.watchList.indexOfFirst {
        it.name == viewModel.currentWatchlist.value?.name
    }
    private var items: List<WatchList> = viewModel.watchList

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
                selectedPosition = adapterPosition
                viewModel.currentWatchlist.postValue(watchList)

                dismissAction.invoke()
            }

            editButton.isVisible = !watchList.isDefault
            editButton.setOnClickListener {
                dismissAction.invoke()
                itemView.context.showEditWatchlistDialog(viewModel, watchList)
            }

            deleteButton.isVisible = !watchList.isDefault
            deleteButton.setOnClickListener {
                dismissAction.invoke()
                itemView.context.showDeleteWatchlistDialog(viewModel, watchList)
            }
        }
    }
}