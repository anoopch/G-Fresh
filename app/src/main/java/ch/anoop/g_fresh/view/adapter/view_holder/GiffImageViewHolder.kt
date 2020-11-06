package ch.anoop.g_fresh.view.adapter.view_holder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.custom.BounceInterpolator
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.imageResource

/**
 * ViewHolder for the GIFF item.
 */
class GiffImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * View elements from the layout
     */
    private val gifImageView by lazy { itemView.findViewById<ImageView>(R.id.giffy_img_view) }
    private val gifTitle by lazy { itemView.findViewById<TextView>(R.id.giffy_name_txt_view) }
    private val favImageView by lazy { itemView.findViewById<ImageView>(R.id.giffy_fav_img_view) }

    /**
     * Binds info to the views.
     */
    fun bind(currentGiffItem: GiffItem, favoriteClickListener: FavoriteClickListener) {

        // Fav button click
        favImageView.apply {

            setOnClickListener {
                favoriteClickListener.onFavoriteButtonClicked(currentGiffItem, adapterPosition)

                // Animation for button
                val myAnim: Animation = loadAnimation(favImageView.context, R.anim.fav_view_bounce)

                // Use bounce interpolator with amplitude 0.3 and frequency 24.0
                myAnim.interpolator = BounceInterpolator(0.3, 24.0)

                favImageView.startAnimation(myAnim)
            }

            // Set image selected based on isFavorite flag
            imageResource =
                if (currentGiffItem.isFavorite) R.drawable.ic_favorite else R.drawable.ic_no_favorite
        }

        // Giff Loading using Glide
        Glide.with(itemView.context)
            .asGif()
            .error(R.drawable.ic_error)
            .load(currentGiffItem.images.fixed_width_downsampled.url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .centerCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade(200))
            .into(gifImageView)

        // Sets the title of the Image
        gifTitle.text = currentGiffItem.title
    }
}