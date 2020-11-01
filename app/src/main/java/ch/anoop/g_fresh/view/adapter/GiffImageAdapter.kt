package ch.anoop.g_fresh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.view_holder.GiffImageViewHolder
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import okhttp3.internal.toImmutableList


class GiffImageAdapter(private val favoriteClickListener: FavoriteClickListener) :
    RecyclerView.Adapter<GiffImageViewHolder>(), FavoriteClickListener {

    private var trendingGiffList = mutableListOf<GiffItem>()
    private var allFavoriteGiffIdsList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiffImageViewHolder {
        return GiffImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.recycler_item_gif_image, parent, false
                )
        )
    }

    override fun getItemCount() = trendingGiffList.size

    override fun onBindViewHolder(holder: GiffImageViewHolder, position: Int) {

        if (allFavoriteGiffIdsList.isNotEmpty()) {
            trendingGiffList[position].isFavorite =
                allFavoriteGiffIdsList.contains(trendingGiffList[position].id)
        }

        holder.bind(trendingGiffList[position], this)
    }

    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        clickedGiffImage.isFavorite = !clickedGiffImage.isFavorite
        trendingGiffList[adapterPosition] = clickedGiffImage
        notifyItemChanged(adapterPosition)

        favoriteClickListener.onFavoriteButtonClicked(clickedGiffImage, adapterPosition)
    }

    fun updateNewItems(newTrends: List<GiffItem>, clearExistingList: Boolean) {

        if (clearExistingList) {
            trendingGiffList.clear()
            notifyDataSetChanged()
        }
        trendingGiffList.addAll(newTrends)

        // Remove duplicates
        val set = trendingGiffList.toImmutableList()
        trendingGiffList.clear()

        trendingGiffList.addAll(
            set.distinctBy { it.id }
        )

        notifyItemRangeInserted(trendingGiffList.size + 1, newTrends.size)
    }

    fun updateFavIds(allFavoriteGiffIdsList: List<String>) {
        this.allFavoriteGiffIdsList.clear()
        this.allFavoriteGiffIdsList.addAll(allFavoriteGiffIdsList)
        notifyDataSetChanged()
    }

}