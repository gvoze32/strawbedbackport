# Multi-target migration guide

This project keeps one implementation in `src/main` and generates loader/version
sources with Stonecutter. Add a target by extending the target matrix, then keep
version-specific resources isolated to that target.

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

## Version-specific Java code

Keep API differences in the shared source with Stonecutter conditions. The
condition is a comment and is removed during generation:

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
./gradlew :1.20.1-forge:runServer --no-configuration-cache \\
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
