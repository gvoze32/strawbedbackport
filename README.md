Backports the Straw Bed from the upcoming Minecraft updates to older versions.

A Straw Bed is a consumable utility block that allows you to sleep through the night without resetting your home spawn point.

## Features

- Craft a Straw Bed from 3 Hay Bales placed horizontally in a crafting table
- Sleep through the night anywhere without overriding your original spawn point
- The bed automatically breaks when you wake up (consumable)
- Safe to use in the Nether or The End (it just breaks safely without exploding)
- Place it in a composter for a 65% chance to raise the compost level

## Supported Versions

| Minecraft | Loaders |
| --- | --- |
| 1.18.2 | Forge, Fabric |
| 1.19.2 | Forge, Fabric |
| 1.20.1 | Forge, Fabric |
| 1.21.1 | Fabric, NeoForge |
| 1.21.11 | Fabric, NeoForge |
| 26.1.2 | Fabric, NeoForge |
| 26.2 | Fabric, NeoForge |

## Development

The target matrix is maintained with Stonecraft and Stonecutter. Shared gameplay
and mixin logic live in `src/main`; loader entrypoint branches use Stonecutter
conditions, and loader metadata stays in shared resources with target
placeholders. Builds compile the version-processed Java output for each target.

## FAQ

**Can I use this in my modpack?**
Yes! Feel free to include this mod in any Modrinth or CurseForge modpack.

**Which loaders are supported?**
Each version is published with the loaders listed above.

## License

This project is licensed under the [MIT License](LICENSE).
