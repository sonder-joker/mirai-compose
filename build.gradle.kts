@file:Suppress("SuspiciousCollectionReassignment")

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21-2"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("org.jetbrains.compose") version "0.3.0-build147"
}

group = "com.younger"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

val CORE_VERSION = "2.0.0"
val CONSOLE_VERSION = "2.0.0"

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("net.mamoe:mirai-core:$CORE_VERSION") // mirai-core µÄ API
    implementation("net.mamoe:mirai-console:$CONSOLE_VERSION") // ºó¶Ë

    testImplementation("net.mamoe:mirai-console-terminal:$CONSOLE_VERSION")
//    runtimeOnly("net.mamoe:mirai-login-solver-selenium:1.0-dev-15")
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
            targetFormats(/*TargetFormat.Dmg, */TargetFormat.Msi/*, TargetFormat.Deb*/)
            packageName = "MiraiCompose"
            version = "0.1.0"
            vendor = "Noire"
        }
    }
}