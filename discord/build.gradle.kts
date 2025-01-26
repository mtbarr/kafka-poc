plugins {
  alias(libs.plugins.kotlin.jvm) apply true
  alias(libs.plugins.kotlin.serialization) apply true
}

dependencies {
  implementation(project(":domain"))

  implementation(libs.kotlinx.serialization)
  implementation(libs.coroutines.core)

  implementation(libs.javacord)

  implementation(libs.kafka.clients)
}