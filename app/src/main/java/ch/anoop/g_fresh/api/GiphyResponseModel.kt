package ch.anoop.g_fresh.api


/**
 * Data classes representing the response of trending and searching GIFFs Giphy API
 */

data class GiphyResponse(val data: List<GiffItem>)

data class GiffItem(val title: String, val images: Image)

data class Image(
    val fixed_width: ImageSize,
    val fixed_height: ImageSize,
    val original: ImageSize
)

data class ImageSize(val url: String)