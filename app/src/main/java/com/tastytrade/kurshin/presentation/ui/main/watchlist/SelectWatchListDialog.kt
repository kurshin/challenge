package com.tastytrade.kurshin.presentation.ui.main.watchlist

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

class SelectWatchListDialog(private val context: Context, private val viewModel: MainViewModel) {

    private val dialog: Dialog = Dialog(context)
    private val view: View = LayoutInflater.from(context).inflate(R.layout.select_watchlist_dialog, null)

    private val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    private val newWatchlistButton: Button = view.findViewById(R.id.btnNewList)

    init {
        dialog.setContentView(view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        recyclerView.adapter = SelectWatchListAdapter(viewModel) {
            dialog.dismiss()
        }

        newWatchlistButton.setOnClickListener {
            context.showEditWatchlistDialog(viewModel)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}
