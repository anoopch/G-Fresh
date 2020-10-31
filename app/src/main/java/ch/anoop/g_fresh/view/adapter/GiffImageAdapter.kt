package ch.anoop.g_fresh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.view_holder.GiffImageViewHolder

class GiffImageAdapter : RecyclerView.Adapter<GiffImageViewHolder>() {

    private val trendingGiffList = mutableListOf<GiffItem>()

    override fun onBindViewHolder(holder: GiffImageViewHolder, position: Int) {
        holder.bind(trendingGiffList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiffImageViewHolder {
        return GiffImageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.recycler_item_gif_image, parent, false
                )
        )
    }

    override fun getItemCount() = trendingGiffList.size

    fun updateNewTrends(newTrends: List<GiffItem>, clearExistingList: Boolean) {

        if (clearExistingList) {
            trendingGiffList.clear()
            notifyDataSetChanged()
        }

        trendingGiffList.addAll(newTrends)
        notifyItemRangeInserted(trendingGiffList.size + 1, newTrends.size)
    }
}