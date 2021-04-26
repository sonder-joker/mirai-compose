import org.jetbrains.compose.desktop.application.dsl.TargetFormat

version = Versions.mirai_compose

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}
configurations.all {
//    this.resolutionStrategy.failOnVersionConflict()
    this.resolutionStrategy {
        this.setForcedModules(
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"
        )
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(decompose)
    implementation(`decompose-extension`)

    implementation(`mirai-core`)
    implementation(`mirai-console`)

    implementation(yamlkt)
    implementation(compose.materialIconsExtended)
    implementation(koin)

    //may remove in future
    implementation("com.google.zxing:core:3.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    testImplementation("io.insert-koin:koin-test:${Versions.koin}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.compose.ui:ui-test-desktop:${Versions.compose}")
    testImplementation("org.jetbrains.compose.ui:ui-test:${Versions.compose}")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MiraiComposeLoader"
        nativeDistributions {
            modules(*jdkModules)
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            packageVersion = Versions.mirai_compose
            vendor = "Noire"


            macOS {
                bundleID = "com.youngerhousea.miraicompose"
                iconFile.set(project.file("icons/mirai.icns"))
            }

            linux {
                iconFile.set(project.file("icons/mirai.png"))
            }

            windows {
                iconFile.set(project.file("icons/mirai.ico"))
                upgradeUuid = "01BBD7BE-A84F-314A-FA84-67B63728A416"
            }
        }
    }
}
