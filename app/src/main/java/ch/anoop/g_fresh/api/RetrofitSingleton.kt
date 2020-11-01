package ch.anoop.g_fresh.api

import ch.anoop.g_fresh.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Retrofit Singleton that handles all the network setup for API calls.
 */

object RetrofitSingleton {

    private const val BASE_URL = "https://api.giphy.com/v1/gifs/"
    private const val TIMEOUT_LIMIT_IN_MILLIS = 50000L // 50 seconds

    private var retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()

        // intercept to add query param to all calls
        okHttpClientBuilder.addInterceptor(QueryParamInterceptor())

        // Set connection time outs
        okHttpClientBuilder.connectTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)
        okHttpClientBuilder.readTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)
        okHttpClientBuilder.writeTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)

        // Log the responses for DEBUG build apps
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        okHttpClientBuilder.addInterceptor(loggingInterceptor)

        return okHttpClientBuilder.build()
    }

    fun getGiphyApiService(): GiphyApiService = retrofit.create(GiphyApiService::class.java)
}