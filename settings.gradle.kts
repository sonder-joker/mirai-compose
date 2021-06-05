rootProject.name = "mirai-compose"

include (":app")
include(":test-plugin")
include(":web")

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")