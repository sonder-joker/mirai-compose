import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.jetbrains.compose")
}

dependencies {
    api(libs.decompose.main)
    implementation(libs.decompose.extension)

    api(libs.mirai.core)
    api(libs.mirai.console)

    implementation(libs.serialization.yaml)

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)

    implementation(libs.zxing.core)

    implementation("org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${libs.versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${libs.versions.kotlin}")

    testImplementation(libs.junit4.core)
    testImplementation(libs.junit4.kotlin)
    testImplementation(libs.kotlin.test)

    implementation(projects.core)

    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
}


compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.app.MiraiComposeLoader"
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)

            packageName = "mirai-compose"
            packageVersion = rootProject.version.toString()
            vendor = rootProject.group as String

            macOS {
                bundleID = "com.youngerhousea.miraicompose"
            }

            linux {
            }

            windows {
                dirChooser = true
                upgradeUuid = "01BBD7BE-A84F-314A-FA84-67B63728A416"
            }
        }
    }
}


kotlin {
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.util.ConsoleExperimentalApi")
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.utils.MiraiExperimentalApi")
        languageSettings.useExperimentalAnnotation("net.mamoe.mirai.console.ConsoleFrontEndImplementation")
        languageSettings.useExperimentalAnnotation("androidx.compose.foundation.ExperimentalFoundationApi")
    }
}