pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.neoforged.net/releases/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("gg.meza.stonecraft") version "1.12.6"
    id("dev.kikugie.stonecutter") version "0.9.7"
}

stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true
    shared {
        version("1.18.2-forge", "1.18.2")
        version("1.18.2-fabric", "1.18.2")
        version("1.19.2-forge", "1.19.2")
        version("1.19.2-fabric", "1.19.2")
        version("1.20.1-forge", "1.20.1")
        version("1.20.1-fabric", "1.20.1")
        version("1.21.1-fabric", "1.21.1")
        version("1.21.1-neoforge", "1.21.1")
        version("1.21.11-fabric", "1.21.11")
        version("1.21.11-neoforge", "1.21.11")
        version("26.1.2-fabric", "26.1.2")
        version("26.1.2-neoforge", "26.1.2")
        version("26.2-fabric", "26.2")
        version("26.2-neoforge", "26.2")
        vcsVersion = "1.21.1-neoforge"
    }
    create(rootProject)
}

rootProject.name = "strawbedbackport"
