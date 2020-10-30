package ch.anoop.g_fresh.model

import com.bumptech.glide.util.Util

data class GifImage(val url: String, val width: Int, val height: Int) {

    override fun hashCode(): Int {
        var result = url.hashCode() ?: 17
        result = 31 * result + width
        result = 31 * result + height
        return result
    }

    override fun toString(): String {
        return "GifImage {url='$url', w=$width, h=$height}"
    }

    override fun equals(other: Any?): Boolean {
        if (other is GifImage) {
            val (url1, width1, height1) = other
            return width1 == width && height1 == height && Util.bothNullOrEqual(url, url1)
        }
        return false
    }
}