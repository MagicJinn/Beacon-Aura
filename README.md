# Beacon Aura

## Extend Beacon Effects and Enhance Your Minecraft Experience

[![Modrinth: Beacon Aura](https://img.shields.io/badge/Modrinth-Beacon-Aura-00ae5d?logo=modrinth)](https://modrinth.com/mod/beacon-aura)
[![CurseForge: Beacon Aura](https://img.shields.io/badge/CurseForge-Beacon-Aura-f16437?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/beacon-aura)

Vanilla beacons feel underwhelming? Beacon Aura solves this by allowing you to *soak* in the beacon's effects while within its range, extending their duration, to be enjoyed20 after you leave their radius. The strength of the effect buildup and the maximum duration are determined by the beacon's level. The mod also increases the beacon's range by default, as well as allowing you to configure it for yourself.

### Features

* **Extended Effect Duration:** Accumulate beacon effects while in range, enjoying them even when you move out of range.
* **Customizable Limits:** Configure maximum effect durations per beacon level.
* **Adjustable Range:** Modify both the base range and the level-based range increase of the beacon's effects.

### Configuration

You can configure the following values with [ModMenu](https://github.com/TerraformersMC/ModMenu) and [YetAnotherConfigLib](https://github.com/isXander/YetAnotherConfigLib), or by editing `config\beacon-aura.json`.

* **Seconds Per Level:** The number of seconds added to the beacon effect's duration per beacon level, per 4 seconds. (Default: 4)
* **Maximum Minutes Per Level:** The maximum duration, in minutes, that the beacon effect can last for each level. (Default: 15)
* **Range Base:** The base range, in blocks, of the beacon effect. (Default: 32, Vanilla: 10)
* **Range Per Level:** The additional range, in blocks, added to the beacon effect's radius for each beacon level. (Default: 32, Vanilla: 10)
