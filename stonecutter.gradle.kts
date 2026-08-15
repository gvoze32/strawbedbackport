import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.register
plugins {
    id("dev.kikugie.stonecutter")
    id("gg.meza.stonecraft")
}

stonecutter active "1.21.1-neoforge" /* [SC] DO NOT EDIT */

tasks.register<Sync>("collectUploadArtifacts") {
    group = "publishing"
    description = "Builds every platform variant and collects uploadable JARs in build/upload."
    dependsOn(stonecutter.tasks.named("build"))

    into(layout.buildDirectory.dir("upload"))
    includeEmptyDirs = false
    from(rootProject.projectDir) {
        include("versions/*-fabric/build/libs/*-fabric-*.jar")
        include("versions/*-forge/build/libs/*-forge-*.jar")
        include("versions/*-neoforge/build/libs/*-neoforge-*.jar")
        eachFile { path = name }
    }
}
