package ch.anoop.g_fresh.api


/**
 * Data classes representing the response of trending and searching GIFFs Giphy API
 */

data class GiphyResponse(val data: List<GiffItem>)

data class GiffItem(val title: String, val images: Image, var isFavorite: Boolean = false)

data class Image(
    val fixed_width_downsampled: ImageSize
)

data class ImageSize(val height: String, val width: String, val url: String)