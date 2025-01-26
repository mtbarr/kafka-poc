package io.github.mtbarr.exchange.job

import io.github.mtbarr.exchange.producer.ExchangeRatioProducer
import io.github.mtbarr.exchange.ratio.collectAllRatios
import io.github.mtbarr.exchange.service.ExchangeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class ExchangeRatioCollectorJob(
  private val exchangeRatioProducer: ExchangeRatioProducer,
  private val exchangeService: ExchangeService,
  private val topic: String = "exchange-ratio",
  private val interval: Duration = 30.seconds,
  private val currencyCode: String = "USD",
  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob()),
) {


  fun start() = coroutineScope.launch {
    println("Starting exchange ratio collector job")
    while (isActive) {
      println("Collecting exchange ratios for $currencyCode")
      val exchangeRatio = exchangeService.fetchExchangeRatio(currencyCode)
      exchangeRatio.collectAllRatios().forEach {
        exchangeRatioProducer.sendExchangeRatio(topic, it)
        println("Sent exchange ratio: $it")
      }
      delay(interval)
    }
  }
}