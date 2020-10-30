package ch.anoop.g_fresh.model

import java.util.*

/** A POJO mirroring the top level result JSON object returned from Giphy's api.  */
class SearchResult {
    private lateinit var data: Array<GifResult>
    override fun toString(): String {
        return "SearchResult{" + "data=" + Arrays.toString(data) + '}'
    }
}