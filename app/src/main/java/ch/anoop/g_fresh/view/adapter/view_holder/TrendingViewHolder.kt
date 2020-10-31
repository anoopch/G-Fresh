package ch.anoop.g_fresh.view.adapter.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * ViewHolder for the trending GIFF item.
 */
class TrendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val gifImageView by lazy { itemView.findViewById<ImageView>(R.id.giffy_img_view) }
    private val gifTitle by lazy { itemView.findViewById<TextView>(R.id.giffy_name_txt_view) }
    private val favImageView by lazy { itemView.findViewById<TextView>(R.id.giffy_fav_img_view) }

    /**
     * Binds data to view holder views.
     */
    fun bind(data: GiffItem) {
        favImageView.setOnClickListener {
            println("Fav clicked")
        }

        Glide.with(itemView.context)
            .asGif()
            .load(data.images.fixed_width.url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(gifImageView)

        gifTitle.text = data.title
    }
}