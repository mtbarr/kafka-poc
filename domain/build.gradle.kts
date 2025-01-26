plugins {
  alias(libs.plugins.kotlin.jvm) apply true
  alias(libs.plugins.kotlin.serialization) apply true
}

dependencies {
  implementation(libs.kotlinx.serialization)
}