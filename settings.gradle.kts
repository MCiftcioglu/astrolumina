pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://chaquo.com/maven") }
    }
    // Chaquopy plugin DSL ile değil, buildscript üzerinden yüklenecek → burada plugins bloğuna gerek yok
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://chaquo.com/maven") }
    }
}

rootProject.name = "AstroLumina"
include(":app")
