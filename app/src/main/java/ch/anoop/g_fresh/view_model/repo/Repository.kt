package ch.anoop.g_fresh.view_model.repo

/**
 * Application repository for single point data operations
 *
 * -- combine/merge if more than one DataSource is present using Single.merge() or similar
 * -- RestfulDataSource and may bbe LocalDataSource later
 */
class Repository(
    private val restfulDataSource: RestfulDataSource
) {

    /**
     *  Loads currently trending Giffs
     *  Offset denotes how many items to skip
     */
    fun loadTrending(offset: Int) = restfulDataSource.loadTrendingGiffs(offset)

    /**
     *  Loads Giffs matching the current search query
     *  Offset denotes how many items to skip
     */
    fun loadSearch(query: String, offset: Int) = restfulDataSource.searchGiffs(query, offset)
}