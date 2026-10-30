
package com.colin.elytratrackermod;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class ElytraConfigScreen {

    public static Screen create(Screen parent) {

        ElytraConfig config = ElytraConfig.HANDLER.instance();

        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Elytra Tracker Settings"))

            .category(
                ConfigCategory.createBuilder()
                    .name(Component.literal("HUD"))

                    .group(
                        OptionGroup.createBuilder()
                            .name(Component.literal("Display Settings"))

                            .option(
                                Option.<Boolean>createBuilder()
                                    .name(Component.literal("Enable HUD"))
                                    .description(
                                        OptionDescription.of(
                                            Component.literal(
                                                "Enable or disable the Elytra Tracker HUD."
                                            )
                                        )
                                    )
                                    .binding(
                                        true,
                                        () -> config.hudEnabled,
                                        value -> config.hudEnabled = value
                                    )
                                    .controller(
                                        TickBoxControllerBuilder::create
                                    )
                                    .build()
                            )

                            .option(
                                Option.<ElytraConfig.DurabilityDisplay>createBuilder()
                                    .name(Component.literal("Durability Display"))
                                    .description(
                                        OptionDescription.of(
                                            Component.literal(
                                                "Choose between remaining durability percentage and the vanilla durability bar."
                                            )
                                        )
                                    )
                                    .binding(
                                        ElytraConfig.DurabilityDisplay.PERCENTAGE,
                                        () -> config.durabilityDisplay,
                                        value -> config.durabilityDisplay = value
                                    )
                                    .controller(
                                        option -> EnumControllerBuilder.create(option)
                                            .enumClass(ElytraConfig.DurabilityDisplay.class)
                                            .valueFormatter(value ->
                                                Component.literal(
                                                    value == ElytraConfig.DurabilityDisplay.PERCENTAGE
                                                        ? "Percentage"
                                                        : "Vanilla Bar"
                                                )
                                            )
                                    )
                                    .build()
                            )

                            .build()
                    )

                    .build()
            )

            .save(() -> ElytraConfig.HANDLER.save())
            .build()
            .generateScreen(parent);
    }
}
