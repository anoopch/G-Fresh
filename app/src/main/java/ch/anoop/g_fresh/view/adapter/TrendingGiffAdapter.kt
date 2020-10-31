package ch.anoop.g_fresh.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.adapter.view_holder.TrendingViewHolder

class TrendingAdapter : RecyclerView.Adapter<TrendingViewHolder>() {

    private val trendingGiffList = mutableListOf<GiffItem>()

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(trendingGiffList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        return TrendingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.recycler_item_gif_image, parent, false
                )
        )
    }

    override fun getItemCount() = trendingGiffList.size

    fun updateNewTrends(newTrends: List<GiffItem>) {
        trendingGiffList.addAll(newTrends)
        notifyItemRangeInserted(trendingGiffList.size + 1, newTrends.size)
    }
}