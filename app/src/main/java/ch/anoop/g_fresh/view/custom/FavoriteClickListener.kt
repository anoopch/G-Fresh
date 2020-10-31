package ch.anoop.g_fresh.view.custom

import androidx.annotation.NonNull
import ch.anoop.g_fresh.api.GiffItem

interface FavoriteClickListener {
    fun onFavoriteButtonClicked(@NonNull clickedGiffImage: GiffItem)
}