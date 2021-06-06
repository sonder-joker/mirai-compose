plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.decompose.main)
    implementation(libs.decompose.extension)

    implementation(libs.mirai.core)
    implementation(libs.mirai.console)

}
