package ch.anoop.g_fresh.model

import com.bumptech.glide.util.Util

/**
 * A POJO mirroring an individual GIF image returned from Giphy's api.
 *
 *
 * Implements equals and hashcode so that in memory caching will work when this object is used
 * as a model for loading Glide's images.
 */
data class GifResult(
    var id: String? = null,
    var images: GifUrlSet? = null
) {
    override fun hashCode(): Int {
        var result = if (id != null) id.hashCode() else 17
        result = 31 * result + if (images != null) images.hashCode() else 17
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other is GifResult) {
            return Util.bothNullOrEqual(id, other.id) && Util.bothNullOrEqual(images, other.images)
        }
        return false
    }

    override fun toString(): String {
        return "GifResult{id='$id', images=$images}"
    }
}