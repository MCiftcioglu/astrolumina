pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://chaquo.com/maven") // ✅ Chaquopy için gerekli
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://chaquo.com/maven") // ✅ Chaquopy için gerekli
        maven("https://jitpack.io")
    }
}

rootProject.name = "AstroLumina"
include(":app")