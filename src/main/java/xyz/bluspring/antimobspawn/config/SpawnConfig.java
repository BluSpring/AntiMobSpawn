package xyz.bluspring.antimobspawn.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.common.ForgeConfigSpec;

public class SpawnConfig {
    public final ForgeConfigSpec.BooleanValue allowNormalSpawn;
    public final ForgeConfigSpec.BooleanValue allowSpawners;
    public final ForgeConfigSpec.BooleanValue allowSpawnEggs;
    public final ForgeConfigSpec.BooleanValue allowConversions;
    public final ForgeConfigSpec.BooleanValue removeAggressively;

    public SpawnConfig(ResourceLocation id, ForgeConfigSpec.Builder builder) {

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

    public boolean canSpawn (MobSpawnType reason) {

        if (this.removeAggressively.get()) {

            return false;
        }

        if (reason == MobSpawnType.SPAWNER) {

            return this.allowSpawners.get();
        }

        if (reason == MobSpawnType.SPAWN_EGG) {

            return this.allowSpawnEggs.get();
        }

        if (reason == MobSpawnType.CONVERSION) {

            return this.allowConversions.get();
        }

        return this.allowNormalSpawn.get();
    }
}
