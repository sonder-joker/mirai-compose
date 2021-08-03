plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.composejb)
    implementation(libs.plugin.jvm)
    implementation(libs.plugin.miraiconsole)
//    implementation(libs.plugin.vaadin)
//    implementation(libs.plugin.gretty)
//    implementation(libs.plugin.grettyJ)

}

kotlin {
    sourceSets.all {
        languageSettings.useExperimentalAnnotation("kotlin.Experimental")
        languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
    }
}





