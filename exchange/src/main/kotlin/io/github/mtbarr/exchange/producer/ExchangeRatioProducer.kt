package io.github.mtbarr.exchange.producer

import io.github.mtbarr.exchange.ratio.ExchangeRatio
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord

class ExchangeRatioProducer(
  private val producer: Producer<String, String>,
  private val json: Json
) {

  fun sendExchangeRatio(topicName: String, ratio: ExchangeRatio) {
    producer.send(
      ProducerRecord(
        topicName,
        ratio.fromCurrency,
        json.encodeToString<ExchangeRatio>(ratio)
      )
    )
  }
}