package io.github.mtbarr.exchange

import io.github.mtbarr.exchange.consumer.ExchangeConsumer
import io.github.mtbarr.exchange.discord.Discord
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.javacord.api.DiscordApiBuilder
import java.util.Properties


fun main() {
  val botToken = System.getenv("DISCORD_BOT_TOKEN")
    ?: error("DISCORD_BOT_TOKEN is not set")

  DiscordApiBuilder()
    .setToken(botToken)
    .login()
    .whenComplete { currentApi, throwable ->
      if (throwable != null) {
        println("Failed to login to Discord")
        throwable.printStackTrace()
        return@whenComplete
      }

      println("Logged in to Discord as ${currentApi.yourself.name}")
      println("Discord invite: ${currentApi.createBotInvite()}")

      val consumerProperties = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.GROUP_ID_CONFIG, "exchange-bot")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
      }

      Discord.api = currentApi
      Discord.currentConsumer = ExchangeConsumer(
        api = currentApi,
        consumer = KafkaConsumer(consumerProperties)
      ).also(ExchangeConsumer::start)
    }
}