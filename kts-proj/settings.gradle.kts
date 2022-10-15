pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }
        mavenCentral()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }

        mavenCentral()
        maven {
            name = "spigotmc-repo"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }
        maven {
            name = "sonatype"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        //version catalog https://docs.gradle.org/current/userguide/platforms.html
        versionCatalogs {
            create("libs") {


            }
        }
    }
}
enableFeaturePreview("VERSION_CATALOGS")
rootProject.name = "kts-proj"

include(":tutor")