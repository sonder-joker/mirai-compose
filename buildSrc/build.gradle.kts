import org.gradle.accessors.dm.LibrariesForLibs

@Suppress("UnstableApiUsage")
val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs") as LibrariesForLibs

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.composejb)
    implementation(libs.plugin.jvm)
    implementation(libs.plugin.miraiconsole)
}

kotlin {
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}





