package xyz.bluspring.antimobspawn.config;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.neoforge.common.ModConfigSpec;

public class SpawnConfig {
    public final ModConfigSpec.BooleanValue allowNormalSpawn;
    public final ModConfigSpec.BooleanValue allowSpawners;
    public final ModConfigSpec.BooleanValue allowSpawnEggs;
    public final ModConfigSpec.BooleanValue allowConversions;
    public final ModConfigSpec.BooleanValue removeAggressively;

    public SpawnConfig(Identifier id, ModConfigSpec.Builder builder) {

        builder.comment("Options for the " + id.getNamespace() + " mod.");
        builder.push(id.getNamespace());

        builder.comment("Spawning options for " + id);
        builder.push(id.getPath());

        builder.comment("Should the entity be allowed to spawn normally?");
        this.allowNormalSpawn = builder.define("allowNormalSpawning", true);

        builder.comment("Should spawners be able to spawn the entity?");
        this.allowSpawners = builder.define("allowSpawners", true);

        builder.comment("Should spawn eggs be able to spawn the entity?");
        this.allowSpawnEggs = builder.define("allowSpawnEggs", true);

        builder.comment("Should the entity spawn via mob conversion? i.e. villager -> zombie");
        this.allowConversions = builder.define("allowConversions", true);

        builder.comment("When enabled the entity type will be aggresively removed from worlds. This will bypass all other options.");
        this.removeAggressively = builder.define("removeAggressively", false);

        builder.pop();
        builder.pop();
    }

    public boolean canSpawn (EntitySpawnReason reason) {

        if (this.removeAggressively.get()) {

            return false;
        }

        if (reason == EntitySpawnReason.SPAWNER) {

            return this.allowSpawners.get();
        }

        if (reason == EntitySpawnReason.SPAWN_ITEM_USE) {

            return this.allowSpawnEggs.get();
        }

        if (reason == EntitySpawnReason.CONVERSION) {

            return this.allowConversions.get();
        }

        return this.allowNormalSpawn.get();
    }
}
