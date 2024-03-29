plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.composejb)
    implementation(libs.plugin.jvm)
    implementation(libs.plugin.buildconfig)
//    implementation(libs.plugin.miraiconsole)

}

kotlin {
    sourceSets.all {
        languageSettings.optIn("kotlin.Experimental")
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}





