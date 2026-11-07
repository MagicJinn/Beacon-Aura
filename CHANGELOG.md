# Changelog

This page documents the changes and fixes made in Beacon Aura, in reverse chronological order.

## 1.3

- Removed beacon range extension functionality and config options.
- Removed effects spanning the full world height functionality and config options.
- Reworked the mixin for better compatibility with other beacon mods. Duration stacking and hidden beacon particles remain. Beacon range is vanilla again, so pair this with a range mod if you want that back. Now plays nicer with mods like [BeaconSaturation](https://github.com/yungando/BeaconSaturation).
- Fixed an error when Mod Menu is installed but YACL isn't. The config button only appears when YACL is present, else it fails gracefully.

## 1.2.1

Minecraft 26.*

- Update to 26.3

## 1.2.0

Minecraft 26.*

- Update to 26.2

## 1.1.0

Minecraft 26.1.*

- Update to include 26.1.2

## 1.0.5

Minecraft 26.1

- 26.1 support
- Moved to Mojang deobfuscated source, from FabricMC Yarn

## 1.0.4

Minecraft 1.21.*

- Disables beacon particles
- Fixes [effects being in-place upgraded, instead of replaced](https://github.com/MagicJinn/Beacon-Aura/issues/4)
- Fixes [range in the config being different from the range specified in the readme](https://github.com/MagicJinn/Beacon-Aura/issues/5)

## 1.0.3

Minecraft 1.21.* + 1.21.9

- Minor update because isClient decided to go private

## 1.0.2

Minecraft 1.21.*

- Fixes [beacon effect range not extending downwards to bedrock](https://github.com/MagicJinn/Beacon-Aura/issues/3)

## 1.0.1

Minecraft 1.21.*

- Config images, yay

## 1.0.0

Minecraft 1.21.*

- Initial release
