package io.github.mtbarr.exchange.service

import io.github.mtbarr.exchange.currency.isValidCurrencyCode
import io.github.mtbarr.exchange.ratio.MultiExchangeRatioResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ExchangeService(
  private val json: Json,
) {

  companion object {
    private val API_URL = System.getenv("API_URL") ?: error("API_URL is not set")
  }

  private val httpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .build()
  }


  suspend fun fetchExchangeRatio(currencyCode: String): MultiExchangeRatioResponse = withContext(Dispatchers.IO) {
    if (!currencyCode.isValidCurrencyCode()) {
      throw IllegalArgumentException("Invalid currency code")
    }

    println("Fetching exchange ratio for $currencyCode")

    val request = Request.Builder()
      .url("$API_URL/latest/$currencyCode")
      .build()

    val response: Response = httpClient.newCall(request).execute()
    val responseBody = response.body?.string() ?: error("Failed to decode response")

    json.decodeFromString(responseBody)
  }

}