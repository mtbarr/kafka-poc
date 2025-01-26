package io.github.mtbarr.exchange

import io.github.mtbarr.exchange.job.ExchangeRatioCollectorJob
import io.github.mtbarr.exchange.producer.ExchangeRatioProducer
import io.github.mtbarr.exchange.service.ExchangeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

fun main() {
  val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
  }

  val producerProperties = Properties().apply {
    put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG , true)
  }

  println("Starting exchange application")
  val exchangeRatioProducer = ExchangeRatioProducer(KafkaProducer(producerProperties), json)
  val exchangeService = ExchangeService(json)

  println("Starting exchange ratio collector job")
  val job = ExchangeRatioCollectorJob(exchangeRatioProducer, exchangeService)

  runBlocking {
    val start = job.start()

    Runtime.getRuntime().addShutdownHook(Thread {
      println("Shutting down exchange application")
      start.cancel()
    })

    start.join()
  }
}

