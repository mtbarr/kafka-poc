package io.github.mtbarr.exchange.discord

import io.github.mtbarr.exchange.consumer.ExchangeConsumer
import org.javacord.api.DiscordApi

object Discord {

  internal lateinit var api: DiscordApi
  internal lateinit var currentConsumer: ExchangeConsumer

  val currentOrThrow get() = api
}