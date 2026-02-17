package xyz.bluspring.antimobspawn.config;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigManager {
    private final ModContainer owner;
    private final ModConfig.Type type;
    private final String name;
    private final ModConfigSpec spec;
    public final ModConfig config;

    private boolean forgeRegistered = false;

    public ConfigManager(ModConfigSpec spec) {

        this(ModConfig.Type.COMMON, spec);
    }

    public ConfigManager(ModConfig.Type type, ModConfigSpec spec) {

        this(FabricLoader.getInstance().getModContainer("antimobspawn").get(), type, spec);
    }

    public ConfigManager(ModContainer owner, ModConfig.Type type, ModConfigSpec spec) {

        this(owner, type, defaultConfigName(type, owner.getMetadata().getId()), spec);
    }

    public ConfigManager(ModContainer owner, ModConfig.Type type, String name, ModConfigSpec spec) {

        this.owner = owner;
        this.type = type;
        this.name = name;
        this.spec = spec;

        if (!FabricLoader.getInstance().isModLoaded("connectormod")) {
            // ForgeConfigAPIPort
            this.config = ConfigTracker.INSTANCE.registerConfig(this.type, this.spec, this.owner.getMetadata().getId(), this.name);
        } else {
            // Forge + Connector
            try {
                var modLoadingContextClass = Class.forName("net.minecraftforge.fml.ModLoadingContext");
                var modContainerClass = Class.forName("net.minecraftforge.fml.ModContainer");
                var contextMethod = modLoadingContextClass.getDeclaredMethod("get");
                var containerMethod = modLoadingContextClass.getDeclaredMethod("getActiveContainer");
                var context = contextMethod.invoke(null);

                var container = containerMethod.invoke(context);

                var modConfigConstructor = ModConfig.class.getDeclaredConstructor(ModConfig.Type.class, IConfigSpec.class, modContainerClass, String.class);
                this.config = modConfigConstructor.newInstance(this.type, this.spec, container, this.name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ConfigManager open () {
        if (FabricLoader.getInstance().isModLoaded("connectormod")) {
            ConfigTracker.INSTANCE.loadConfigs(type, FabricLoader.getInstance().getConfigDir());
        }
        //ConfigTracker.INSTANCE.loadConfigs(type, FabricLoader.getInstance().getConfigDir());

        return this;
    }

    public ConfigManager registerWithForge () {

        if (!this.forgeRegistered) {
            if (FabricLoader.getInstance().isModLoaded("connectormod")) {
                try {
                    var modLoadingContextClass = Class.forName("net.minecraftforge.fml.ModLoadingContext");
                    var contextMethod = modLoadingContextClass.getDeclaredMethod("get");
                    var containerMethod = modLoadingContextClass.getDeclaredMethod("getActiveContainer");
                    var context = contextMethod.invoke(null);

                    var registerConfigMethod = modLoadingContextClass.getDeclaredMethod("registerConfig", ModConfig.Type.class, IConfigSpec.class);
                    registerConfigMethod.invoke(context, type, spec);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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
