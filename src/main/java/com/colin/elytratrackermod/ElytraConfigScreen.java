
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

    @SuppressWarnings("deprecation")
    public static Screen create(Screen parent) {

        ElytraConfig config = ElytraConfig.HANDLER.instance();

        // Create the Bar Color option first so the durability
        // display option can control its availability.
        Option<ElytraConfig.BarColorMode> barColorOption =
            Option.<ElytraConfig.BarColorMode>createBuilder()
                .name(Component.literal("Bar Color"))
                .description(
                    OptionDescription.of(
                        Component.literal(
                            "Choose the color scheme for the durability bar."
                        )
                    )
                )
                .binding(
                    ElytraConfig.BarColorMode.VANILLA,
                    () -> config.barColorMode,
                    value -> config.barColorMode = value
                )
                .controller(
                    option -> EnumControllerBuilder.create(option)
                        .enumClass(ElytraConfig.BarColorMode.class)
                        .valueFormatter(value ->
                            Component.literal(
                                value == ElytraConfig.BarColorMode.VANILLA
                                    ? "Vanilla"
                                    : "Spectrum"
                            )
                        )
                )
                .build();

        // Set initial availability based on the saved display mode.
        barColorOption.setAvailable(
            config.durabilityDisplay
                == ElytraConfig.DurabilityDisplay.VANILLA_BAR
        );

        // Create the durability display option.
        Option<ElytraConfig.DurabilityDisplay> durabilityDisplayOption =
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
                // Dynamically enable or disable Bar Color.
                .listener((option, value) ->
                    barColorOption.setAvailable(
                        value == ElytraConfig.DurabilityDisplay.VANILLA_BAR
                    )
                )
                .build();

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
                                    ).append(Component.literal(
                                        "\n\nThe HUD display will appear dynamically on the left or right depending on the current Minecraft Main Hand setting."
                                    ))
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

                            .option(durabilityDisplayOption)

                            .option(barColorOption)

                            .build()
                    )

                    .build()
            )

            .save(() -> ElytraConfig.HANDLER.save())
            .build()
            .generateScreen(parent);
    }
}
