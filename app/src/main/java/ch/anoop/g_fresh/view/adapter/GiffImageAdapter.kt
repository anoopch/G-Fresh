package ch.anoop.g_fresh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.view_holder.GiffImageViewHolder
import ch.anoop.g_fresh.view.custom.FavoriteClickListener

class GiffImageAdapter(private val favoriteClickListener: FavoriteClickListener) :
    RecyclerView.Adapter<GiffImageViewHolder>(), FavoriteClickListener {

    private val trendingGiffList = mutableListOf<GiffItem>()

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
        holder.bind(trendingGiffList[position], this)
    }

    fun updateNewItems(newTrends: List<GiffItem>, clearExistingList: Boolean) {

        if (clearExistingList) {
            trendingGiffList.clear()
            notifyDataSetChanged()
        }

        trendingGiffList.addAll(newTrends)
        notifyItemRangeInserted(trendingGiffList.size + 1, newTrends.size)
    }

    override fun onFavoriteButtonClicked(clickedGiffImage: GiffItem, adapterPosition: Int) {
        clickedGiffImage.isFavorite = !clickedGiffImage.isFavorite
        trendingGiffList[adapterPosition] = clickedGiffImage
        notifyItemChanged(adapterPosition)

        favoriteClickListener.onFavoriteButtonClicked(clickedGiffImage, adapterPosition)
    }
}