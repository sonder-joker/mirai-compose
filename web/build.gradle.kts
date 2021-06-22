plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(compose.runtime)
    implementation(projects.core)
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("io.netty:netty-all:4.1.65.Final")
    implementation("io.vertx:vertx-core:4.1.0")
    implementation("io.vertx:vertx-lang-kotlin:4.1.0")
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}