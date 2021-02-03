@file:Suppress("SuspiciousCollectionReassignment")

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21-2"
    id("org.jetbrains.compose") version "0.3.0-build147"
}

group = "com.younger"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("net.mamoe:mirai-core:2.3.2") // mirai-core µÄ API
    implementation("net.mamoe:mirai-console:2.3.2") // ºó¶Ë
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "15"
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-Xjvm-default=enable"
    }
}

kotlin.sourceSets.all {
    languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.ConsoleFrontEndImplementation")
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraidesktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            version = "0.1.0"
            vendor = "Noire"

            macOS {
                iconFile.set(project.file("mirai.ico"))
            }
            windows {
                iconFile.set(project.file("mirai.ico"))
            }
            linux {
                iconFile.set(project.file("mirai.png"))
            }
        }
    }
}