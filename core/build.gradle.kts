import org.jetbrains.compose.compose


plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.decompose.main)
    implementation(libs.decompose.extension)

    implementation(libs.mirai.core)
    implementation(libs.mirai.console)

    implementation(libs.serialization.json)
    implementation(libs.serialization.yaml)

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common:1.5.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.10")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")

    testImplementation(libs.junit4.core)
    testImplementation(libs.junit4.kotlin)
    testImplementation(libs.kotlin.test)

    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
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
