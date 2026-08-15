import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    id("gg.meza.stonecraft")
}

val stonecutter = extensions.getByType<StonecutterBuildExtension>()

modSettings {
    variableReplacements.put("license", "MIT")
    variableReplacements.put(
        "itemIdentifier",
        if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) "id" else "item"
    )
}
