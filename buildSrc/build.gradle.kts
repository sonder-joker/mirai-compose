import org.gradle.kotlin.dsl.`kotlin-dsl`

repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
}

kotlin {
    sourceSets.all {
//        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
//        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}

