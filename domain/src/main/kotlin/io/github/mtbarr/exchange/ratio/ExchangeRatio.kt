package io.github.mtbarr.exchange.ratio

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ExchangeRatio(
  val uniqueId: String = UUID.randomUUID().toString(),
  val fromCurrency: String,
  val toCurrency: String,
  val ratio: Double,
  val timeStamp: Long = System.currentTimeMillis(),
)