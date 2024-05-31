package com.tastytrade.kurshin.presentation.ui.main.watchlist
import android.content.Context
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.tastytrade.kurshin.R
import com.tastytrade.kurshin.domain.WatchList
import com.tastytrade.kurshin.presentation.ui.main.MainViewModel

fun Context.showAddWatchlistDialog(userViewModel: MainViewModel) {
    val editTextWatchlistName = EditText(this).apply {
        hint = context.getString(R.string.enter_watchlist_name)
        val margin = resources.getDimensionPixelSize(R.dimen._16dp)
        layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(margin, margin / 2, margin, 0)
        }
    }

    AlertDialog.Builder(this)
        .setView(FrameLayout(this).apply { addView(editTextWatchlistName) })
        .setPositiveButton(R.string.add) { dialog, _ ->
            val watchlistName = editTextWatchlistName.text.toString()
            if (watchlistName.isNotBlank()) {
                userViewModel.addWatchList(WatchList(watchlistName))
            }
            dialog.dismiss()
        }
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}