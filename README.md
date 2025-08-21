# Beacon Aura

![icon.png](icon.png)

## Improves beacons by allowing you to soak in a beacon's effects with duration stacking over time

[![Modrinth: Beacon Aura](https://img.shields.io/badge/Modrinth-Beacon_Aura-00ae5d?logo=modrinth)](https://modrinth.com/mod/beacon-aura)
[![CurseForge: Beacon Aura](https://img.shields.io/badge/CurseForge-Beacon_Aura-f16437?logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/beacon-aura)

Beacons are some of the hardest items to obtain, but they feel underwhelming and are only circumstantially useful. **Beacon Aura** solves this by allowing you to *soak* in the beacon's effects while within its range, stacking duration over time, so you can enjoy their effects long after you leave their radius. The speed and cap of the effects buildup is determined by the beacon's level.

**Beacon Aura** also increases the default beacon radius from 10 to 32 per level, as well as allowing you to configure it yourself.

### Features

* **Extended Effect Duration:** Accumulate beacon effects while in range, to be enjoyed while out of range.
* **Customizable Limits:** Configure maximum effect durations per beacon level, as well as effect buildup speed.
* **Adjustable Beacon Range:** Modify both the base range and the level-based range increase of the beacon's effects.

### Configuration

You can configure the following values with [ModMenu](https://github.com/TerraformersMC/ModMenu) and [YetAnotherConfigLib](https://github.com/isXander/YetAnotherConfigLib), or by editing `config\beacon-aura.json`.

* **Seconds Per Level:** The number of seconds added to the beacon effect's duration per beacon level, per 4 seconds. (Default: 4)
* **Maximum Minutes Per Level:** The maximum duration, in minutes, that the beacon effect can last for each level. (Default: 15)
* **Range Base:** The base range, in blocks, of the beacon effect. (Default: 32, Vanilla: 10)
* **Range Per Level:** The additional range, in blocks, added to the beacon effect's radius for each beacon level. (Default: 32, Vanilla: 10)
