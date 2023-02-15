package xyz.bluspring.antimobspawn;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;

import java.util.HashMap;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            var builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal("AntiMobSpawn Config"));

            builder.setSavingRunnable(() -> {
                AntiMobSpawn.config.manager.config.save();
            });

            var category = builder.getOrCreateCategory(Component.literal(""));
            var perModSubCategories = new HashMap<String, SubCategoryBuilder>();

            for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
                var spawnConfig = AntiMobSpawn.config.configs.get(entityType);
                if (spawnConfig == null)
                    continue;

                var entry = builder.entryBuilder().startSubCategory(entityType.getDescription());

                entry.add(
                    builder.entryBuilder()
                        .startBooleanToggle(Component.literal("Allow conversions?"), spawnConfig.allowConversions.get())
                            .setDefaultValue(spawnConfig.allowConversions.getDefault())
                            .setSaveConsumer(spawnConfig.allowConversions::set)
                        .build()
                );

                entry.add(
                        builder.entryBuilder()
                                .startBooleanToggle(Component.literal("Allow normal spawning?"), spawnConfig.allowNormalSpawn.get())
                                .setDefaultValue(spawnConfig.allowNormalSpawn.getDefault())
                                .setSaveConsumer(spawnConfig.allowNormalSpawn::set)
                                .build()
                );

                entry.add(
                        builder.entryBuilder()
                                .startBooleanToggle(Component.literal("Allow spawning from mob spawners?"), spawnConfig.allowSpawners.get())
                                .setDefaultValue(spawnConfig.allowSpawners.getDefault())
                                .setSaveConsumer(spawnConfig.allowSpawners::set)
                                .build()
                );

                entry.add(
                        builder.entryBuilder()
                                .startBooleanToggle(Component.literal("Allow spawning from spawn eggs?"), spawnConfig.allowSpawnEggs.get())
                                .setDefaultValue(spawnConfig.allowSpawnEggs.getDefault())
                                .setSaveConsumer(spawnConfig.allowSpawnEggs::set)
                                .build()
                );

                entry.add(
                        builder.entryBuilder()
                                .startBooleanToggle(Component.literal("Aggressively remove from world?"), spawnConfig.removeAggressively.get())
                                .setDefaultValue(spawnConfig.removeAggressively.getDefault())
                                .setSaveConsumer(spawnConfig.removeAggressively::set)
                                .build()
                );

                var location = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);

                perModSubCategories.computeIfAbsent(
                        location.getNamespace(),
                        (namespace) -> {
                            var modContainer = FabricLoader.getInstance().getModContainer(namespace);

                            return builder.entryBuilder().startSubCategory(
                                    Component.literal(
                                            modContainer.isPresent() ? modContainer.get().getMetadata().getName() : namespace
                                    )
                            );
                        }
                ).add(entry.build());
            }

            for (String namespace : perModSubCategories.keySet().stream().sorted().toList()) {
                var subCategoryBuilder = perModSubCategories.get(namespace);

                category.addEntry(subCategoryBuilder.build());
            }

            return builder.build();
        };
    }
}
