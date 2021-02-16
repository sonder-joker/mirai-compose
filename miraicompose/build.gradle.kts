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

    implementation(`mirai-core`)
    implementation(`mirai-console`)
//    runtimeOnly("net.mamoe:mirai-login-solver-selenium:1.0-dev-16")

    implementation(yamlkt)
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MiraiConsoleComposeLoader"
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            version = Versions.mirai_compose
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


