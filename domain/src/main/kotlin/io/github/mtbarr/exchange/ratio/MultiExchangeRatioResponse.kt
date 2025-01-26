package io.github.mtbarr.exchange.ratio

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiExchangeRatioResponse(
  val result: String,
  val documentation: String,
  @SerialName("terms_of_use")
  val termsOfUse: String,
  @SerialName("time_last_update_unix")
  val timeLastUpdateUnix: Long,
  @SerialName("time_last_update_utc")
  val timeLastUpdateUtc: String,
  @SerialName("time_next_update_unix")
  val timeNextUpdateUnix: Long,
  @SerialName("time_next_update_utc")
  val timeNextUpdateUtc: String,
  @SerialName("base_code")
  val baseCode: String,
  @SerialName("conversion_rates")
  val conversionRates: Map<String, Double>,
)


fun MultiExchangeRatioResponse.collectAllRatios(): List<ExchangeRatio> {
  val baseCurrency = baseCode
  return conversionRates.map { (toCurrency, ratio) ->
    ExchangeRatio(fromCurrency = baseCurrency, toCurrency = toCurrency, ratio = ratio)
  }
}