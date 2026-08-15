# Multi-target migration guide

This project uses Stonecraft (`gg.meza.stonecraft`) on top of Stonecutter
(`dev.kikugie.stonecutter`). Stonecraft configures the loader/version Gradle
subprojects; Stonecutter preprocesses shared source and creates each target.
Add a target by extending the matrix, then keep version-specific resources
isolated to that target.

## How Stonecraft is wired

| File | Responsibility |
| --- | --- |
| `settings.gradle.kts` | Applies Stonecraft and Stonecutter, declares target subprojects, and calls `create(rootProject)`. |
| `build.gradle.kts` | Applies Stonecraft and configures `modSettings`, including resource variable replacements. |
| `stonecutter.gradle.kts` | Stores Stonecutter's active target marker. The `/* [SC] DO NOT EDIT */` line is managed by Stonecutter. |
| `versions/dependencies/<version>.properties` | Supplies the Minecraft and loader dependency versions shared by that version's loaders. |
| `src/main` | Common Java, metadata, data, models, textures, and sounds. |
| `versions/<target>/src` | Target-only Java/resource overrides. |

Stonecraft is the configuration layer, not a second source tree: it wires
Architectury/Loom and loader dependencies for each Stonecutter subproject and
provides consistent defaults for metadata, data generation, GameTest, and
publishing integrations. Stonecutter remains responsible for target fan-out
and comment preprocessing.

The current project pins Stonecraft `1.12.6` and Stonecutter `0.9.7` in
`settings.gradle.kts`. Upgrade either plugin as a build-system migration:
read its changelog, regenerate every target, and rerun the complete target
matrix before changing the release version.

`settings.gradle.kts` is the target graph. Each `version("<target>",
"<minecraft>")` entry creates a `minecraft-loader` subproject. The second
argument selects the dependency file; it is not the generated project name.
`create(rootProject)` then applies the shared Stonecraft build to those
subprojects.

`build.gradle.kts` can read the active Stonecutter target through
`StonecutterBuildExtension`. This project uses that value to set
`modSettings.variableReplacements`, so shared JSON can use placeholders such
as `${itemIdentifier}` while the generated target receives `item` or `id`.

The active marker is useful for IDE/source-generation workflows, but normal
builds should address a target explicitly:

```bash
./gradlew :1.20.1-forge:stonecutterGenerate --no-configuration-cache
./gradlew :1.20.1-forge:build --no-configuration-cache
```

For the target recorded in `stonecutter.gradle.kts`, Stonecraft registers
root-level convenience tasks:

```bash
./gradlew buildActive --no-configuration-cache
./gradlew runActive --no-configuration-cache
```

Use the Stonecutter/IDE switch workflow to change that active target. The
`vcsVersion = "1.21.1-neoforge"` setting in `settings.gradle.kts` is the
repository's clean source baseline; it prevents active-version switching from
turning generated source changes into accidental commits. CI and release
commands should still use explicit `:<target>:` tasks so they do not depend on
the active marker.

Do not edit `versions/<target>/build/generated` or the active marker by hand.
Edit `src/main`, the target's `src` override, or the Stonecraft configuration,
then regenerate.

## Target matrix

| Minecraft | Loader | Project path |
| --- | --- | --- |
| 1.18.2 | Forge | `1.18.2-forge` |
| 1.18.2 | Fabric | `1.18.2-fabric` |
| 1.19.2 | Forge | `1.19.2-forge` |
| 1.19.2 | Fabric | `1.19.2-fabric` |
| 1.20.1 | Forge | `1.20.1-forge` |
| 1.20.1 | Fabric | `1.20.1-fabric` |
| 1.21.1 | Fabric | `1.21.1-fabric` |
| 1.21.1 | NeoForge | `1.21.1-neoforge` |
| 1.21.11 | Fabric | `1.21.11-fabric` |
| 1.21.11 | NeoForge | `1.21.11-neoforge` |
| 26.1.2 | Fabric | `26.1.2-fabric` |
| 26.1.2 | NeoForge | `26.1.2-neoforge` |
| 26.2 | Fabric | `26.2-fabric` |
| 26.2 | NeoForge | `26.2-neoforge` |

## Add a version

1. Add one dependency file at `versions/dependencies/<minecraft>.properties`.
   Define `minecraft_version`, the loader version used by the target, and the
   matching `fabric_version` or `neoforge_version`.
2. Add each loader target to the `stonecutter.shared` block in
   `settings.gradle.kts`:

   ```kotlin
   version("<minecraft>-<loader>", "<minecraft>")
   ```

3. Create `versions/<minecraft>-<loader>/` only for target-specific resources
   or data overrides. Shared Java and resources stay in `src/main`.
4. Add the target to the README support matrix.

Stonecutter generates target sources under `versions/<target>/build` during a
build. Generated output is disposable and must not be committed.

## Add a loader to an existing version

For example, adding NeoForge to an already supported Minecraft version is a
small, explicit graph change:

1. Add `neoforge_version=...` to that version's dependency properties.
2. Add `version("<minecraft>-neoforge", "<minecraft>")` inside
   `stonecutter.shared`.
3. Add the NeoForge branch to loader-specific registration and event wiring in
   `src/main/java`.
4. Confirm the shared `neoforge.mods.toml` placeholders resolve for the new
   subproject. Add a target resource only when its schema differs.
5. Generate, compile, and smoke-test the new subproject before changing the
   release matrix.

Do not copy the entire common source tree into a version directory. The
subproject inherits `src/main`; only the delta belongs under
`versions/<target>/src`.

## Stonecraft resource and metadata processing

Stonecraft applies `modSettings.variableReplacements` while generating each
subproject. This project defines the `itemIdentifier` replacement in
`build.gradle.kts`:

```kotlin
variableReplacements.put(
    "itemIdentifier",
    if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) "id" else "item"
)
```

The shared recipe consumes it as `${itemIdentifier}`. The same mechanism is
used for `${id}`, `${version}`, `${minecraftVersion}`, `${fabricVersion}`, and
`${neoforgeVersion}` in generated metadata. Keep placeholders in shared
resources; do not hard-code a target value into a common file.

Stonecraft/Stonecutter output is split into:

- `versions/<target>/build/generated/stonecutter/main/java` for preprocessed
  Java;
- `versions/<target>/build/generated/stonecutter/main/resources` for
  preprocessed resources;
- `versions/<target>/build/resources/main` for the final resource classpath;
- `versions/<target>/build/libs` for the release jar.

Inspect generated output to diagnose a preprocessing or replacement error, but
make the fix in the shared source/configuration and regenerate.

## Stonecraft source preprocessing

Stonecraft runs Stonecutter's preprocessor during
`:<target>:stonecutterGenerate`. Guarded code is selected before Java
compilation; it is not a runtime `if` and it must not leave references to an
API that does not exist in the generated target.

This repository uses the line-comment guard style below:

```java
//? if >=1.21.11 {
/*return modernApi();
*///?} else {
return legacyApi();
//?}
```

Stonecraft's upstream examples also show the equivalent block-comment form
(`/*? if fabric {*/ ... /*?}*/`). This repository uses the line-comment form
above because it is already used throughout `src/main`; keep one delimiter
style balanced within each guard and follow the surrounding file.

The same guards can wrap imports, class declarations, methods, fields, and
individual statements. Keep a guard's opening and closing markers balanced.
Nested loader/version guards are valid and are used in the registry classes.
Commented code is not necessarily dead code: it may be the active branch for
another target.

Common predicates in this project are:

| Predicate | Selects |
| --- | --- |
| `fabric` | Fabric targets |
| `forge` | Forge targets |
| `neoforge` | NeoForge targets |
| `>=1.20.5`, `>=1.21.11`, `>=26.1` | Version/API boundaries |

Use the existing guard style rather than creating duplicate source files for
each loader. When a branch becomes large or depends on a target-only resource,
move that resource into `versions/<target>/src` instead.

## Version compatibility boundaries

Keep API differences in the shared source with Stonecraft conditions. The
condition is evaluated during generation and removed from the target source:

```java
//? if >=1.21.11 {
/* new API branch *///?} else {
/* legacy API branch */
//?}
```

Use the narrowest boundary that explains the API change. Current boundaries
include:

- `<1.20` for `Material`, old Forge setup, and pre-1.20 registration APIs.
- `>=1.20.5` for `useWithoutItem` and the modern recipe result key.
- `>=1.21.11` for `Identifier`, environment attributes, and registry-keyed
  block/item properties.
- `>=26.1` for the current Fabric creative-tab and overlay APIs.
- `<26.2` for the `BedBlock.newBlockEntity` compatibility override.

Before adding a branch, inspect the mapped Minecraft and loader sources for the
exact target. Do not widen a condition because two versions happen to compile;
verify both generated source and runtime behavior.

## Loader boundaries

- Fabric registers directly through `Registry`/`BuiltInRegistries` and uses
  Fabric item-group events where available.
- NeoForge uses `DeferredRegister`, mod-bus registration, and NeoForge gameplay
  events.
- Forge uses `DeferredRegister`, `MinecraftForge` events, and Forge sound
  types.
- Shared gameplay stays in `StrawBedBlock` and `StrawBedTracker`; loader classes
  only wire registration and events.

A new loader must preserve these contracts:

1. Register the block before its `BlockItem` is initialized.
2. Register custom sounds before block sound properties are consumed.
3. Register the custom statistic before awarding it after wake-up.
4. Cancel the spawn-set event only for a successful straw-bed sleep attempt.
5. Remove both bed halves on wake-up or invalid dimensions.

## Resource overrides

Put a target-only resource at
`versions/<target>/src/main/resources/...`. Use this for schema changes rather
than contaminating the shared resource with a parser-incompatible format.

The recipe is the current example:

- pre-1.20.5 targets use the `item` result key;
- 1.20.5+ targets use the `id` result key;
- 26.x and 1.21.11 targets use string ingredient values;
- older targets keep the object ingredient form.

The generated artifact must contain exactly one resource at each logical path.
Build the target and inspect the jar when a schema changes.

## Build and smoke-test

Use Java 21 and run the relevant target task:

```bash
./gradlew :1.20.1-forge:build --no-configuration-cache
./gradlew :1.20.1-forge:runServer --no-configuration-cache \
  --args "nogui --world verify-1.20.1-forge"
```

For a complete release build:

```bash
./gradlew clean build --no-configuration-cache
```

For each target, the smoke test must show a `Done (...s)!` server line with no
current `ERROR`, `Couldn't parse`, or unknown-registry messages. Stop the server
cleanly after the check. Build outputs remain under `versions/*/build/libs/` and
are release inputs, not source files.

## Troubleshooting Stonecraft

- **Target project is missing:** verify the exact
  `version("<minecraft>-<loader>", "<minecraft>")` entry and the matching
  `versions/dependencies/<minecraft>.properties` file.
- **Generated code is stale:** run:

  ```bash
  ./gradlew :<target>:stonecutterPrepare :<target>:stonecutterGenerate \
    --rerun-tasks --no-configuration-cache
  ```
- **A dependency is missing:** inspect the generated source first, then check
  the loader-specific property key (`forge_version`, `neoforge_version`, or
  `fabric_version`) and the repositories in `settings.gradle.kts`.
- **A JSON placeholder is unresolved:** check `modSettings.variableReplacements`
  in `build.gradle.kts`; inspect the generated resource, not the shared
  placeholder file.
- **A guard branch compiles on one target only:** inspect the generated Java
  under `build/generated/stonecutter/main/java` and tighten the version or
  loader predicate.
- **The IDE shows the wrong target:** update the Stonecutter active target
  through the Stonecutter workflow; do not remove or hand-edit the
  `/* [SC] DO NOT EDIT */` marker.

## Stonecraft and Stonecutter references

- [Stonecraft overview](https://github.com/meza/stonecraft/blob/main/docs/docs/01-Stonecraft.mdx)
- [Stonecraft quickstart](https://github.com/meza/stonecraft/blob/main/docs/docs/02-Quickstart.mdx)
- [Stonecraft `modSettings` configuration](https://github.com/meza/stonecraft/blob/main/docs/docs/03-configuration/01-modsettings/index.md)
- [Stonecutter documentation](https://stonecutter.kikugie.dev/)

## Stonecraft publishing

Stonecraft adds the Mod Publish Plugin's `publishMods` task to every generated
target. The tasks are target-scoped:

```bash
./gradlew :1.20.1-forge:publishMods --no-configuration-cache
./gradlew :1.20.1-fabric:publishMods --no-configuration-cache
```

This repository intentionally does not commit Modrinth/CurseForge credentials
or service-specific publishing configuration. Configure those secrets and
metadata in the publishing setup before invoking `publishMods`. The published
GitHub release uses the jars from `versions/*/build/libs` and is independent
of those optional services.

## Release checklist

1. Update `mod.version` in `gradle.properties` only when the release version is
   decided.
2. Run the complete build and the target smoke tests.
3. Verify the jar name, loader metadata, recipe, and mod entrypoint for every
   target.
4. Remove generated build directories before committing source changes.
5. Commit the source and matrix changes, push the branch, then publish one
   release with the per-target jars attached.
6. Record any target-specific override in this guide and in the README matrix.
