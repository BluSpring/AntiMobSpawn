package xyz.bluspring.antimobspawn.config;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Configuration {

    public final ConfigManager manager;
    public final Map<EntityType<?>, SpawnConfig> configs = new HashMap<>();

    public Configuration() {

        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        for (final EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
            this.configs.put(type, new SpawnConfig(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(type)), builder));
        }

        this.manager = new ConfigManager(builder.build());
        this.manager.registerWithForge();
        this.manager.open();
    }

    public boolean allowSpawn (Entity entity, EntitySpawnReason reason) {

        final SpawnConfig config = this.configs.get(entity.getType());

        if (config == null) {
            return true;
        }

        else {
            return config.canSpawn(reason);
        }
    }
}