plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.3"
}
