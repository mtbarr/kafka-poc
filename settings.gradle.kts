plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"

}
rootProject.name = "kafka-poc"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("exchange", "domain", "discord")