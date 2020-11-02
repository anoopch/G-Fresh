package ch.anoop.g_fresh.view.custom

import androidx.annotation.NonNull
import ch.anoop.g_fresh.api.GiffItem

/**
 * Listener interface for handling the Favorite button click
 */
interface FavoriteClickListener {
    fun onFavoriteButtonClicked(@NonNull clickedGiffImage: GiffItem, @NonNull adapterPosition: Int)
}