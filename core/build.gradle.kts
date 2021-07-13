plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.decompose.main)

    implementation(libs.mirai.core)
    implementation(libs.mirai.console)

    implementation(libs.serialization.yaml)

    implementation(libs.kotlin.stdlib.main)
    implementation(libs.kotlin.stdlib.common)
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.reflect)
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.4")

    testImplementation(libs.junit4.core)
    testImplementation(libs.junit4.kotlin)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutinues.test)
}

kotlin {
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.util.ConsoleExperimentalApi")
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.utils.MiraiExperimentalApi")
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.ConsoleFrontEndImplementation")
    }
}

project.afterEvaluate {
    apply<MiraiComposeHelper>()
}
