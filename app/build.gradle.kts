import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.github.gmazzo.buildconfig")
}

dependencies {
    api(libs.mirai.core)
    api(libs.mirai.console)

    implementation(libs.serialization.yaml)

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)
    implementation(compose.desktop.components.splitPane)

    implementation(libs.zxing)

    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-common"))
    implementation(kotlin("stdlib-jdk7"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    testImplementation(libs.junit4)
    testImplementation(kotlin("test-junit"))
    testImplementation(kotlin("test"))

    testImplementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
}

buildConfig {
    buildConfigField("String", "projectName", "\"${MiraiCompose.name}\"")
    buildConfigField("String", "projectGroup", "\"${MiraiCompose.group}\"")
    buildConfigField("String", "projectVersion", "\"${MiraiCompose.version}\"")
}

compose.desktop {
    application {
        mainClass = MiraiCompose.mainClass
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)

            packageName = MiraiCompose.name
            packageVersion = MiraiCompose.version
            vendor = MiraiCompose.group

            macOS {
                bundleID = MiraiCompose.group
            }

            linux {
            }

            windows {
                dirChooser = true
                upgradeUuid = MiraiCompose.windowsUUID
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


