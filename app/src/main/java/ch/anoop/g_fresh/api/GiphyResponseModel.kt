package ch.anoop.g_fresh.api

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data classes representing the response of  Giphy API
 * Same response for both trending and searching API
 *
 * Class also contains Annotations for Room
 */

data class GiphyResponse(val data: List<GiffItem>)

@Entity(tableName = "fav_giffs")
data class GiffItem(
    @NonNull @PrimaryKey val id: String,
    val title: String,
    @Embedded
    val images: Image,
    var isFavorite: Boolean = false
)

data class Image(
    @Embedded
    val fixed_width_downsampled: ImageSize
)

data class ImageSize(val height: String, val width: String, val url: String)