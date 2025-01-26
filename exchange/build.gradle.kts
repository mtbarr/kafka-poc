plugins {
  alias(libs.plugins.kotlin.jvm) apply true
  alias(libs.plugins.kotlin.serialization) apply true
}

dependencies {
  implementation(project(":domain"))

  implementation(libs.kotlinx.serialization)
  implementation(libs.coroutines.core)

  implementation(libs.kafka.clients)

  implementation(libs.okhttp.core)
  implementation(libs.okhttp.logging)
}