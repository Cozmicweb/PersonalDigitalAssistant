<p align="center">
  <img width="170" height="170" src="https://i.imgur.com/Mkkl8gz.gif">
</p>

<h1 align="center">Personal Display Assistant</h1>

<p align="center">
  <a href="https://modrinth.com/mod/personal-digital-assistant">
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/modrinth_vector.svg">
  </a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/personal-digital-assistant/preview">
    <img src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/available/curseforge_vector.svg">
  </a>
</p>

**P**ersonal **D**igital **A**ssistant (**PDA**) is a mod for [NeoForge](https://neoforged.net/) that ports Terraria's [Informational Accessories](https://terraria.wiki.gg/wiki/Informational_Accessories) to Minecraft. Every accessory is toggleable and many of them are customizable both on the client and server.
Find these accessories in Pillager Outposts, Abandoned Mineshafts, Dungeons, and Buried Treasure. Combine them to compact their benefits into a single item.

# Datapack Support
To add existing handlers to more items, a datapack can be used. `data/pda/data_maps/item/display_handlers.json` contains all the information mapping display handlers to items. You can map it one-to-one or use a list of handlers. `data/pda/data_maps/entity_type/rare_mobs.json` contains all entities that will be detected by the Lifeform Analyzer's handler. It also supports one-to-one assignment as well as rudimentary nbt support. See `rare_mobs.json` for examples.

# Depending on PDA
Custom display handlers can be added by extending `InfoDisplayHandler` and following the same registration as stated above.
```
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        // forRepositories(fg.repository) // Uncomment when using ForgeGradle
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

// Standard Gradle dependency
dependencies {
    implementation "${MAVEN_COORDINATES}"
}

// Legacy Loom dependency
dependencies {
    modImplementation "${MAVEN_COORDINATES}"
}
```
Replace ${MAVEN_COORDINATES} with the version you want to use. This can be found under "Developer Information" on any version. For example, version [26.2-v1.0.0](https://modrinth.com/mod/personal-digital-assistant/version/26.2-v1.0.0#:~:text=Modrinth%20Maven%20API.-,Maven%20coordinates%3A,-maven.modrinth%3A8ibwFXBz).

## How It Works
The `InfoDisplayHandler` class has some documentation to help but I will describe the pipeline here as well.

Items are mapped to "display handlers" in `data/pda/data_maps/item/display_handlers.json`. Display handlers extend the abstract class `cozmicweb.pda.common.content.information_display.handlers.InfoDisplayHandler` and usually override the `#getDisplayText()` method. Default handlers are registered in `cozmicweb.pda.common.content.information_display.InfoDisplayManager` with the `#register(Identifier, Class)` method from the common side `@Mod` constructor. The `Identifier` provided when registering is used in `display_handlers.json` to map display handlers to items.

All attempts to get the information to display start on the client. If `#requiresServerSync()` is not overridden or returns `false`, the client will not make any attempt to contact the server for information. If it returns `true`, the client will send a `ServerDataRequestPayload.Request` packet to the server. This packet contains information returned by `#getServerDataParameters()`. In other words, `#getServerDataParameters()` sends information from the client to the server. The packet is then handled internally in `NetworkHandler` where it calls `#handleServerRequest(Player, Object[])` on the server. The `Object[] params` parameter is the information returned by `#getServerDataParameters()` on the client. Information returned by `#handleServerRequest(Player, Object[])` is then sent back to the client with `ServerDataRequestPayload.Response` and calls the client method `#updateServerData(Object[])` with the information. This is where the `serverData` map should be populated. Finally, the `serverData` map can be referenced in `#getDisplayText()` to get information from the server.

`InfoDisplayHandler` also has methods that can be overridden to set its default priority with `#getDefaultPriority()` and tooltip with `#getBehavior()`.

### Note:
- If a display's visibility is disabled in the client config, it's handler will not be called and no packets will be sent.
- `serverData`, `#getUpdateInterval()`, `#getServerDataParameters()`, `#updateServerData(Object[])`, and `handleServerRequest(Player, Object[])` are only used if `#requiresServerSync()` returns true.
