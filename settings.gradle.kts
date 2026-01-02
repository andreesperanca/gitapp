pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "gitapp"
include(":app")
include(":core:presentation")
include(":core:presentation:designsystem")
include(":core:network")
include(":feature-users:data")
include(":feature-users:domain")
include(":feature-users:presentation")
include(":core:database")
include(":feature-repo")
include(":feature-repo:data")
include(":feature-repo:domain")
include(":feature-repo:presentation")
include(":core:common")
