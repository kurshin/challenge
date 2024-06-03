package com.tastytrade.kurshin.presentation.ui.main.watchlist
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.Symbol
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

fun Context.showEditWatchlistDialog(userViewModel: MainViewModel, watchList: WatchList? = null) {
    val editTextWatchlistName = EditText(this).apply {
        hint = context.getString(R.string.enter_watchlist_name)
        val margin = resources.getDimensionPixelSize(R.dimen._16dp)
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(margin, margin / 2, margin, 0)
        }

        watchList?.let { setText(it.name) }
    }

    val dialog = AlertDialog.Builder(this)
        .setView(FrameLayout(this).apply { addView(editTextWatchlistName) })
        .setPositiveButton(if (watchList == null) R.string.create else R.string.save, null)
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()

    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
        val watchlistName = editTextWatchlistName.text.toString()
        if (watchlistName.isNotEmpty()) {
            if (watchList == null) {
                userViewModel.addWatchList(WatchList(watchlistName))
            } else {
                val oldName = watchList.name
                watchList.name = watchlistName
                userViewModel.updateWatchList(watchList)
            }
            dialog.dismiss()
        } else {
            editTextWatchlistName.error = getString(R.string.can_not_be_blank)
        }
    }
}

fun Context.showDeleteWatchlistDialog(userViewModel: MainViewModel, watchList: WatchList) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(getString(R.string.delete_watchlist_confirmation, watchList.name))
        .setPositiveButton(R.string.delete) { _, _ ->
            userViewModel.deleteWatchList(watchList)
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}

fun Context.showDeleteSymbolDialog(userViewModel: MainViewModel, symbol: Symbol, callback:() -> Unit) {
    val dialog = AlertDialog.Builder(this)
        .setTitle(getString(R.string.delete_symbol_confirmation, symbol.name))
        .setPositiveButton(R.string.delete) { _, _ ->
            userViewModel.deleteSymbol(symbol)
            callback.invoke()
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}