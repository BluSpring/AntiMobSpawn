package xyz.bluspring.antimobspawn;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.Entity;
import xyz.bluspring.antimobspawn.config.Configuration;

public class AntiMobSpawn implements ModInitializer {
    public static Configuration config;

    @Override
    public void onInitialize() {
        if (!FabricLoader.getInstance().isModLoaded("connectormod") && !FabricLoader.getInstance().isModLoaded("forgeconfigapiport")) {
            throw new IllegalStateException("ForgeConfigAPIPort is not installed!");
        }

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!config.allowSpawn(entity, null)) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        });

        ServerLifecycleEvents.SERVER_STARTING.register((server) -> {
            if (config == null)
                config = new Configuration();
        });

        EntityEvent.LIVING_CHECK_SPAWN.register((entity, world, x, y, z, type, spawner) -> {
            if (!config.allowSpawn(entity, type)) {
                entity.remove(Entity.RemovalReason.DISCARDED);
                return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });

        EntityEvent.ADD.register((entity, world) -> {
            if (!config.allowSpawn(entity, null)) {
                entity.remove(Entity.RemovalReason.DISCARDED);
                return EventResult.interruptFalse();
            }

            return EventResult.pass();
        });
    }
}
