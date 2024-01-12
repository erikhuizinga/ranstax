import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    id("io.gitlab.arturbosch.detekt")
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

compose.experimental {
    web.application {}
}

detekt {
    config.setFrom(project.rootProject.projectDir.path + "/config/detekt/detekt.yml")
    source.setFrom(
        kotlin.sourceSets.flatMap { kotlinSourceSet ->
            kotlinSourceSet.kotlin.srcDirs
        }.toSet()
    )
}

tasks.check {
    dependsOn("detekt")
}
