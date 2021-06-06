rootProject.name = "mirai-compose"

include (":app")
include(":test-plugin")
include(":web")
include(":core")

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")