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

    private const val API_KEY_PARAM = "api_key"
    private const val API_KEY_VALUE = "IiWPYZdp684tjiCw6mJGT9RgoJLJcSaq"

    private const val LIMIT_PARAM = "limit"
    private const val LIMIT_VALUE = "10"

    private const val BASE_URL = "https://api.giphy.com/v1/gifs/"
    private const val TIMEOUT_LIMIT_IN_MILLIS = 30000L

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

        // Intercept API call and add the API key and limit for Giphy
        okHttpClientBuilder.addInterceptor { chain ->
            val originalRequest = chain.request()

            val newRequestBuilder = originalRequest.newBuilder()
            newRequestBuilder.addHeader(API_KEY_PARAM, API_KEY_VALUE)
            newRequestBuilder.addHeader(LIMIT_PARAM, LIMIT_VALUE)

            val request = newRequestBuilder.build()
            chain.proceed(request)
        }

        // Log the responses for DEBUG build apps
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            okHttpClientBuilder.connectTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.readTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.writeTimeout(TIMEOUT_LIMIT_IN_MILLIS, TimeUnit.MILLISECONDS)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    fun getGiphyApiService(): GiphyApiService = retrofit.create(GiphyApiService::class.java)
}