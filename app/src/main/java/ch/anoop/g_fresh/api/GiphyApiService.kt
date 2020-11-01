package ch.anoop.g_fresh.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface class representing all the API endpoints, HTTP methods
 */
interface GiphyApiService {

    // Sample - http://api.giphy.com/v1/gifs/trending?limit=10&api_key=IiWPYZdp684tjiCw6mJGT9RgoJLJcSaq
    @GET("trending")
    fun fetchLatestTrending(@Query("offset") offset: Int): Single<GiphyResponse>

    //  Sample - http://api.giphy.com/v1/gifs/search?q=java&limit=9&api_key=IiWPYZdp684tjiCw6mJGT9RgoJLJcSaq
    @GET("search")
    fun search(
        @Query("q") searchQuery: String?,
        @Query("offset") offset: Int
    ): Single<GiphyResponse>

}