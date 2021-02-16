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
            modules(
                "java.base",
                "java.compiler",
                "java.datatransfer",
                "java.desktop",
                "java.instrument",
                "java.logging",
                "java.management",
                "java.management.rmi",
                "java.naming",
                "java.net.http",
                "java.prefs",
                "java.rmi",
                "java.scripting",
                "java.se",
                "java.security.jgss",
                "java.security.sasl",
                "java.smartcardio",
                "java.sql",
                "java.sql.rowset",
                "java.transaction.xa",
                "java.xml",
                "java.xml.crypto",
                "jdk.accessibility",
                "jdk.aot",
                "jdk.attach",
                "jdk.charsets",
                "jdk.compiler",
                "jdk.crypto.cryptoki",
                "jdk.crypto.ec",
                "jdk.crypto.mscapi",
                "jdk.dynalink",
                "jdk.editpad",
                "jdk.hotspot.agent",
                "jdk.httpserver",
                "jdk.incubator.foreign",
                "jdk.incubator.jpackage",
                "jdk.internal.ed",
                "jdk.internal.jvmstat",
                "jdk.internal.le",
                "jdk.internal.opt",
                "jdk.internal.vm.ci",
                "jdk.internal.vm.compiler",
                "jdk.internal.vm.compiler.management",
                "jdk.jartool",
                "jdk.javadoc",
                "jdk.jcmd",
                "jdk.jconsole",
                "jdk.jdeps",
                "jdk.jdi",
                "jdk.jdwp.agent",
                "jdk.jfr",
                "jdk.jlink",
                "jdk.jshell",
                "jdk.jsobject",
                "jdk.jstatd",
                "jdk.localedata",
                "jdk.management",
                "jdk.management.agent",
                "jdk.management.jfr",
                "jdk.naming.dns",
                "jdk.naming.rmi",
                "jdk.net",
                "jdk.nio.mapmode",
                "jdk.sctp",
                "jdk.security.auth",
                "jdk.security.jgss",
                "jdk.unsupported",
                "jdk.unsupported.desktop",
                "jdk.xml.dom",
                "jdk.zipfs"
            )
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MiraiCompose"
            version = Versions.compose
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


