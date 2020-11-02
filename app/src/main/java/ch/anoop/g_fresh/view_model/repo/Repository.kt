package ch.anoop.g_fresh.view_model.repo

class Repository(
    private val restfulDataSource: RestfulDataSource
) {

    fun loadTrending(offset: Int) = restfulDataSource.loadTrendingGiffs(offset)

    fun loadSearch(query: String, offset: Int) = restfulDataSource.searchGiffs(query, offset)
}