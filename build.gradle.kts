@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

plugins {
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.serialization") version Versions.kotlin apply false
    id("org.jetbrains.compose") version Versions.compose apply false
//    id("de.undercouch.download") version Versions.download
}

allprojects {
    group = "com.youngerhousea"

    repositories {
        google()
        mavenLocal()
        mavenCentral()
        jcenter()
        maven(url = "https://jitpack.io/")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://packages.jetbrains.team/maven/p/ui/dev")
    }
}

subprojects {
    afterEvaluate {
        apply<Build>()
        configureJvmTarget()
        configureEncoding()
        configureKotlinExperimentalUsages()
        configureKotlinCompilerSettings()
    }
}


val experimentalAnnotations = arrayOf(
    "kotlin.Experimental",
    "kotlin.RequiresOptIn",
    "kotlin.ExperimentalUnsignedTypes",
    "kotlin.ExperimentalStdlibApi",
    "kotlin.experimental.ExperimentalTypeInference",
    "kotlin.io.path.ExperimentalPathApi",

    "net.mamoe.mirai.console.ConsoleFrontEndImplementation",
    "net.mamoe.mirai.console.util.ConsoleExperimentalApi",
    "net.mamoe.mirai.utils.MiraiExperimentalApi",

    "androidx.compose.foundation.ExperimentalFoundationApi"
)

fun Project.configureJvmTarget() {
    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    extensions.findByType(JavaPluginExtension::class.java)?.run {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

fun Project.configureEncoding() {
    tasks.withType(JavaCompile::class.java) {
        options.encoding = "UTF8"
    }
}

fun Project.configureKotlinExperimentalUsages() {
    val sourceSets = kotlinSourceSets ?: return

    for (target in sourceSets)
        target.languageSettings.run {
            progressiveMode = true
            experimentalAnnotations.forEach { a ->
                useExperimentalAnnotation(a)
            }
        }
}

fun Project.configureKotlinCompilerSettings() {
    val kotlinCompilations = kotlinCompilations ?: return
    for (kotlinCompilation in kotlinCompilations) with(kotlinCompilation) {
        if (isKotlinJvmProject) {
            @Suppress("UNCHECKED_CAST")
            this as org.jetbrains.kotlin.gradle.plugin.KotlinCompilation<KotlinJvmOptions>
        }
        kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
    }
}

val Project.kotlinSourceSets get() = extensions.findByName("kotlin").safeAs<KotlinProjectExtension>()?.sourceSets

val Project.isKotlinJvmProject: Boolean get() = extensions.findByName("kotlin") is KotlinJvmProjectExtension


val Project.kotlinTargets
    get() =
        extensions.findByName("kotlin").safeAs<KotlinSingleTargetExtension>()?.target?.let { listOf(it) }
            ?: extensions.findByName("kotlin").safeAs<KotlinMultiplatformExtension>()?.targets

val Project.kotlinCompilations
    get() = kotlinTargets?.flatMap { it.compilations }

