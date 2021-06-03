@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

group = "com.youngerhousea"
version = libs.versions.app.get()

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
    }
    afterEvaluate {
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
)

fun Project.configureJvmTarget() {
    tasks.withType<KotlinJvmCompile>().configureEach {
        kotlinOptions.jvmTarget = "1.8"
    }

    extensions.findByType<JavaPluginExtension>()?.run {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

fun Project.configureEncoding() {
    tasks.withType<JavaCompile>() {
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

