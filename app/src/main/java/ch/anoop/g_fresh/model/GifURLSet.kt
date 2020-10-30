package ch.anoop.g_fresh.model

import com.bumptech.glide.util.Util

/**
 * A POJO mirroring a JSON object with a put of urls of different sizes and dimensions returned
 * for a single image from Giphy's api.
 */
data class GifUrlSet(
    var original: GifImage? = null,
    var fixedWidth: GifImage? = null,
    var fixedHeight: GifImage? = null
) {

    override fun hashCode(): Int {
        var result = if (original != null) original.hashCode() else 17
        result = 31 * result + if (fixedWidth != null) fixedWidth.hashCode() else 17
        result = 31 * result + if (fixedHeight != null) fixedHeight.hashCode() else 17
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other is GifUrlSet) {
            return (Util.bothNullOrEqual(original, other.original)
                    && Util.bothNullOrEqual(fixedWidth, other.fixedWidth)
                    && Util.bothNullOrEqual(fixedHeight, other.fixedHeight))
        }
        return false
    }

    override fun toString(): String {
        return ("GifUrlSet { original=$original, fixed_width=$fixedWidth, fixed_height=$fixedHeight}")
    }
}