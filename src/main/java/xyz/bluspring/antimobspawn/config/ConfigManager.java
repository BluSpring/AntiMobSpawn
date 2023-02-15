package xyz.bluspring.antimobspawn.config;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigManager {
    private final ModContainer owner;
    private final ModConfig.Type type;
    private final String name;
    private final ForgeConfigSpec spec;
    public final ModConfig config;

    private boolean forgeRegistered = false;

    public ConfigManager(ForgeConfigSpec spec) {

        this(ModConfig.Type.COMMON, spec);
    }

    public ConfigManager(ModConfig.Type type, ForgeConfigSpec spec) {

        this(FabricLoader.getInstance().getModContainer("antimobspawn").get(), type, spec);
    }

    public ConfigManager(ModContainer owner, ModConfig.Type type, ForgeConfigSpec spec) {

        this(owner, type, defaultConfigName(type, owner.getMetadata().getId()), spec);
    }

    public ConfigManager(ModContainer owner, ModConfig.Type type, String name, ForgeConfigSpec spec) {

        this.owner = owner;
        this.type = type;
        this.name = name;
        this.spec = spec;

        this.config = new ModConfig(this.type, this.spec, this.owner, this.name);
    }

    public ConfigManager open () {
        //ConfigTracker.INSTANCE.loadConfigs(type, FabricLoader.getInstance().getConfigDir());

        return this;
    }

    public ConfigManager registerWithForge () {

        if (!this.forgeRegistered) {
            //ModLoadingContext.registerConfig("antimobspawn", type, spec);
            this.forgeRegistered = true;
            return this;
        }

        throw new IllegalStateException("The config " + this.config.getFileName() + " has already been registered with Forge.");
    }

    private static String defaultConfigName (ModConfig.Type type, String modId) {

        return String.format("%s-%s.toml", modId, type.extension());
    }
}
