plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.decompose.main)

    implementation(libs.mirai.core)
    implementation(libs.mirai.console)

    implementation(libs.serialization.yaml)

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin}")

    testImplementation(libs.junit4.core)
    testImplementation(libs.junit4.kotlin)
    testImplementation(libs.kotlin.test)
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
