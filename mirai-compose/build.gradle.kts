import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = Versions.mirai_compose

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}


dependencies {
    implementation(compose.desktop.currentOs)
    implementation(decompose)
    implementation(`decompose-extension`)

    implementation(`mirai-core`)
    implementation(`mirai-console`)

    implementation(yamlkt)
    implementation(koin)

    //may remove in future
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.1.0")

    implementation("org.jetbrains.compose.material:material-icons-extended:${Versions.compose}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}")
    testImplementation("org.jetbrains.compose.ui:ui-test-junit4:${Versions.compose}")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}



//configure<ProcessResources>("processResources") {
//    from(sourceSets.getAt("").resources.srcDirs) {
//
//    }
//}

inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
    (this.tasks.getByName(name) as C).configuration()
}


compose.desktop {
    application {
        mainClass = "com.youngerhousea.miraicompose.MiraiComposeKt"
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


tasks {
    val compileKotlin by getting {}

    register("fillConstant") {
        doLast {
            (compileKotlin as KotlinCompile).source.filter { it.name == "MiraiCompose.kt" }.single()
                .let { file ->
                    file.readText()
                        .replace(
                            Regex("""override val version: SemVersion = SemVersion\(.*\)""")
                        ) {
                            """override val version: SemVersion = SemVersion("${project.version}")"""
                        }
                }
        }
    }
}
