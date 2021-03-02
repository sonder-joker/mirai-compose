import org.jetbrains.compose.desktop.application.dsl.TargetFormat

version = Versions.mirai_compose

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(decompose)
    implementation(`decompose-extension`)

    implementation(`mirai-core`)
    implementation(`mirai-console`)

    implementation(yamlkt)
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MiraiConsoleComposeLoader"
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            packageVersion = Versions.mirai_compose
            vendor = "Noire"

            macOS {
                bundleID = "com.youngerhousea.miraicompose"
                iconFile.set(project.file("../icons/mirai.icns"))
            }

            linux {
                iconFile.set(project.file("../icons/mirai.png"))
            }

            windows {
                iconFile.set(project.file("../icons/mirai.ico"))
                upgradeUuid = "01BBD7BE-A84F-314A-FA84-67B63728A416"
            }
        }
    }
}


