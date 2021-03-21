import de.undercouch.gradle.tasks.download.Download
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("de.undercouch.download")
    application
}

val libraryPath = "third_party/java-cef"
val hostOs = System.getProperty("os.name")
val target = when {
    hostOs == "Mac OS X" -> "macos"
    hostOs == "Linux" -> "linux"
    hostOs.startsWith("Win") -> "windows"
    else -> throw Error("Unknown os $hostOs")
}

val cefDownloadZip = run {
    val zipName = "jcef-runtime-$target.zip"
    val zipFile = File("third_party/$zipName")
    tasks.register<Download>("downloadCef") {
        onlyIf { !zipFile.exists() }
        src("https://bintray.com/jetbrains/skija/download_file?file_path=$zipName")
        dest(zipFile)
        onlyIfModified(true)
    }.map { zipFile }
}

val cefUnZip = run {
    val targetDir = File("third_party/java-cef").apply { mkdirs() }
    tasks.register<Copy>("unzipCef") {
        from(cefDownloadZip.map { zipTree(it) })
        into(targetDir)
    }.map { targetDir }
}

tasks.withType<KotlinCompile>().configureEach {
    dependsOn(cefUnZip)
}

dependencies {
    implementation("org.jetbrains.jcef:jcef-skiko:0.1")
    implementation(compose.desktop.currentOs)
}

application {
    applicationDefaultJvmArgs = listOf("-Djava.library.path=$libraryPath")
    mainClassName = "org.jetbrains.compose.desktop.AppKt"
}
