
package com.colin.elytratrackermod;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

public class ElytraConfig {

    public static final ConfigClassHandler<ElytraConfig> HANDLER =
        ConfigClassHandler.createBuilder(ElytraConfig.class)
            .id(Identifier.fromNamespaceAndPath(
                "elytratrackermod",
                "client"
            ))
            .serializer(config ->
                GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance()
                        .getConfigDir()
                        .resolve("elytratrackermod.json5"))
                    .setJson5(true)
                    .build()
            )
            .build();

    @SerialEntry
    public boolean hudEnabled = true;

    @SerialEntry
    public DurabilityDisplay durabilityDisplay =
        DurabilityDisplay.PERCENTAGE;

    public enum DurabilityDisplay {
        PERCENTAGE,
        VANILLA_BAR
    }
}
