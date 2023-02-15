package xyz.bluspring.antimobspawn.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import xyz.bluspring.antimobspawn.AntiMobSpawn;
import xyz.bluspring.antimobspawn.config.Configuration;

public class AntiMobSpawnClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            if (AntiMobSpawn.config == null)
                AntiMobSpawn.config = new Configuration();
        });
    }
}
