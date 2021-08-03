plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
//    id("com.vaadin")
//    id("org.gretty")
}

//gretty {
//    contextPath = "/"
//}

dependencies {
    implementation(projects.core)
//    implementation(libs.vaadin.core)
    api(libs.decompose.main)
}

repositories {
    maven(url= "https://jitpack.io")
}

