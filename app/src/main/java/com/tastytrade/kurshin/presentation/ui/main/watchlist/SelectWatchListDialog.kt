package com.tastytrade.kurshin.presentation.ui.main.watchlist

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

class SelectWatchListDialog(private val context: Context, private val viewModel: MainViewModel) {

    private val dialog: Dialog = Dialog(context)
    private val view: View = LayoutInflater.from(context).inflate(R.layout.select_watchlist_dialog, null)

    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    private val newWatchlistButton: Button = view.findViewById(R.id.btnNewList)
    private val editWatchListsButton: Button = view.findViewById(R.id.btnEditList)
    private val noWatchlistsSelector: Button = view.findViewById(R.id.cbNoWatchlists)

    init {
        dialog.setContentView(view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val watchList = viewModel.watchList
        recyclerView.adapter = SelectWatchListAdapter(watchList)
        editWatchListsButton.isVisible = watchList.isNotEmpty()
        noWatchlistsSelector.isEnabled = watchList.isNotEmpty()

        newWatchlistButton.setOnClickListener {
            context.showAddWatchlistDialog(viewModel)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}
