package io.github.mtbarr.exchange.consumer


import io.github.mtbarr.exchange.ratio.ExchangeRatio
import io.github.mtbarr.exchange.ratio.collectAllRatios
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.Consumer
import org.javacord.api.DiscordApi
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class ExchangeConsumer(
  topicName: String = "exchange-ratio",
  private val api: DiscordApi,
  private val consumer: Consumer<String, String>,
  private val json: Json = Json {
    ignoreUnknownKeys = true
  },
) {

  private val localCache: MutableMap<String, ExchangeRatio> = ConcurrentHashMap()

  private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

  init {
    consumer.subscribe(listOf(topicName))
  }


  fun start(
    currencyCode: String = "USD",
    interval: Duration = 60.seconds,
  ) = coroutineScope.launch {
    println("Starting exchange ratio collector job")
    while (isActive) {
      println("Collecting exchange ratios for $currencyCode")

      pollLatestRatios(currencyCode) {
        localCache[it.toCurrency] = it
      }

      delay(interval)
    }
  }

  fun pollLatestRatios(
    currencyCode: String,
    duration: Duration = 1.seconds,
    action: suspend (ExchangeRatio) -> Unit,
  ) = coroutineScope.launch {

    while (isActive) {
      val records = consumer.poll(duration.toJavaDuration())
      records.forEach {
        val ratio = json.decodeFromString<ExchangeRatio>(it.value())
        action(ratio)
      }

      consumer.commitSync()
    }
  }
}