package ch.anoop.g_fresh.view.adapter.view_holder

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.anoop.g_fresh.R
import ch.anoop.g_fresh.api.GiffItem
import ch.anoop.g_fresh.view.custom.FavoriteClickListener
import ch.anoop.g_fresh.view.custom.MyBounceInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.imageResource


/**
 * ViewHolder for the GIFF item.
 */
class GiffImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val gifImageView by lazy { itemView.findViewById<ImageView>(R.id.giffy_img_view) }
    private val gifTitle by lazy { itemView.findViewById<TextView>(R.id.giffy_name_txt_view) }
    private val favImageView by lazy { itemView.findViewById<ImageView>(R.id.giffy_fav_img_view) }

    /**
     * Binds info to the views.
     */
    fun bind(currentGiffItem: GiffItem, favoriteClickListener: FavoriteClickListener) {
        favImageView.setOnClickListener {
            favoriteClickListener.onFavoriteButtonClicked(currentGiffItem, bindingAdapterPosition)

            val myAnim: Animation = loadAnimation(favImageView.context, R.anim.bounce)

            // Use bounce interpolator with amplitude 0.2 and frequency 20
            val interpolator = MyBounceInterpolator(0.3, 24.0)
            myAnim.interpolator = interpolator

            favImageView.startAnimation(myAnim)
        }
        favImageView.imageResource =
            if (currentGiffItem.isFavorite) R.drawable.ic_favorite else R.drawable.ic_no_favorite

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

        gifTitle.text = currentGiffItem.title
    }
}