package ch.anoop.g_fresh.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 *  Intercept API calls and add the API key and limit for Giphy
 */
class QueryParamInterceptor : Interceptor {

    private val apiKeyParam = "api_key"
    private val apiKeyValue = "IiWPYZdp684tjiCw6mJGT9RgoJLJcSaq"

    private val limitParam = "limit"
    private val limitValue = "10"

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter(apiKeyParam, apiKeyValue)
            .addQueryParameter(limitParam, limitValue)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}