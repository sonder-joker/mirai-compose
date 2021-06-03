import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

version = libs.versions.app

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(libs.decompose.main)
    implementation(libs.decompose.extension)

    implementation(libs.mirai.core)
    implementation(libs.mirai.console)

    implementation(libs.serialization.json)
    implementation(libs.serialization.yaml)

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)

    testImplementation(libs.junit4.core)
    testImplementation(libs.junit4.kotlin)
    testImplementation(libs.kotlin.test)

    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MiraiComposeLoader"
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)

            packageName = "mirai-compose"
            packageVersion = libs.versions.app.get()
            vendor = rootProject.group as String

            macOS {
                bundleID = "com.youngerhousea.miraicompose"
//                iconFile.set(project.file("icons/mirai.icns"))
            }

            linux {
//                iconFile.set(project.file("icons/mirai.png"))
            }

            windows {
                dirChooser = true
//                iconFile.set(project.file("icons/mirai.ico"))
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

//project.afterEvaluate {
//    apply<MiraiComposeHelper>()
//}
