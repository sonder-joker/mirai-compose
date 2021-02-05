@file:Suppress("SuspiciousCollectionReassignment")

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.jetbrains.compose") version "0.3.0-build149"
}

group = "com.youngerhousea"
version = "0.1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

val mirai_version = "2.3.2"
dependencies {
    implementation(compose.desktop.currentOs)
    implementation("net.mamoe:mirai-core:$mirai_version")
    implementation("net.mamoe:mirai-console:$mirai_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-Xjvm-default=enable"
    }
}

kotlin.sourceSets.all {
    languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.ConsoleFrontEndImplementation")
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            version = "0.1.0"
            vendor = "Noire"

            macOS {
                iconFile.set(project.file("icons/mirai.icns"))
            }
            linux {
                iconFile.set(project.file("icons/mirai.png"))
            }
            windows {
                iconFile.set(project.file("icons/mirai.ico"))
                upgradeUuid = "01BBD7BE-A84F-314A-FA84-67B63728A416"
            }
        }
    }
}