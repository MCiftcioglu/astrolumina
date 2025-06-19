pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://chaquo.com/maven")
        maven { url = uri("https://chaquo.com/maven") } // 🔧 Chaquopy plugin deposu

        }

    }


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://chaquo.com/maven")
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://chaquo.com/maven") } // 🔧 Chaquopy dependency deposu
    }
}

rootProject.name = "AstroLumina"
include(":app")
