import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
    mavenLocal()
    mavenCentral()
    maven(url = "https://jitpack.io/")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


kotlin {
    sourceSets.all {
//        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
//        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}




