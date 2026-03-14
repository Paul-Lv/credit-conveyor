rootProject.name = "credit-conveyor"
pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}

include(
    "common-dto",
    "conveyor",
    "deal",
    "application",
    "dossier",
    "gateway",
    "audit",
    "auth"
)
