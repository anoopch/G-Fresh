package ch.anoop.g_fresh.view_model.repo

import ch.anoop.g_fresh.api.GiphyResponse
import io.reactivex.rxjava3.core.Single

/**
 * Interface defining all the methods of data query.
 *
 * Any source of data or the repository should implement this interface
 */
interface DataSource {
    /**
     *  Loads currently trending Giffs
     *  Offset denotes how many items to skip
     */
    fun loadTrendingGiffs(offset: Int): Single<GiphyResponse>

    /**
     *  Loads Giffs matching the current search query
     *  Offset denotes how many items to skip
     */
    fun searchGiffs(query: String, offset: Int): Single<GiphyResponse>
}