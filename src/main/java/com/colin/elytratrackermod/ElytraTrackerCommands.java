package com.colin.elytratrackermod;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ElytraTrackerCommands {

        public static void register() {

                ClientCommandRegistrationCallback.EVENT.register(
                                (dispatcher, registryAccess) -> {

                                        dispatcher.register(
                                                        ClientCommands.literal("etm")

                                                                        // /etm or /etm help
                                                                        .executes(context -> {
                                                                                showHelp(context.getSource());
                                                                                return 1;
                                                                        })

                                                                        .then(
                                                                                        ClientCommands.literal("help")
                                                                                                        .executes(context -> {
                                                                                                                showHelp(context.getSource());
                                                                                                                return 1;
                                                                                                        }))

                                                                        // /etm hud on
                                                                        // /etm hud off
                                                                        .then(
                                                                                        ClientCommands.literal("hud")

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "on")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Elytra Tracker HUD enabled."));

                                                                                                                                                return 1;
                                                                                                                                        }))

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "off")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = false;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Elytra Tracker HUD disabled."));

                                                                                                                                                return 1;
                                                                                                                                        })))
                                                                        .then(

                                                                                        ClientCommands.literal("bar")

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "vanilla")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().durabilityDisplay = ElytraConfig.DurabilityDisplay.VANILLA_BAR;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().barColorMode = ElytraConfig.BarColorMode.VANILLA;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Vanilla durability bar enabled."));

                                                                                                                                                return 1;
                                                                                                                                        }))

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "spectrum")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().durabilityDisplay = ElytraConfig.DurabilityDisplay.VANILLA_BAR;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().barColorMode = ElytraConfig.BarColorMode.SPECTRUM;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Spectrum durability bar enabled."));
                                                                                                                                                return 1;

                                                                                                                                        })))

                                                                        .then(

                                                                                        ClientCommands.literal(
                                                                                                        "durability")

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "percentage")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().durabilityDisplay = ElytraConfig.DurabilityDisplay.PERCENTAGE;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Percentage durability enabled."));

                                                                                                                                                return 1;
                                                                                                                                        }))

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "remaining")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().durabilityDisplay = ElytraConfig.DurabilityDisplay.REMAINING;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Percentage durability enabled."));
                                                                                                                                                return 1;

                                                                                                                                        }))

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "time")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().hudEnabled = true;

                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().durabilityDisplay = ElytraConfig.DurabilityDisplay.TIME;

                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Remaining flight time enabled"));

                                                                                                                                                return 1;
                                                                                                                                        })))

                                                                        .then(
                                                                                        ClientCommands.literal(
                                                                                                        "full")

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "on")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().showDurabilityAtFull = true;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Show durability at 100%."));

                                                                                                                                                return 1;
                                                                                                                                        }))

                                                                                                        .then(
                                                                                                                        ClientCommands.literal(
                                                                                                                                        "off")
                                                                                                                                        .executes(context -> {
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .instance().showDurabilityAtFull = false;
                                                                                                                                                ElytraConfig.HANDLER
                                                                                                                                                                .save();

                                                                                                                                                context.getSource()
                                                                                                                                                                .sendFeedback(
                                                                                                                                                                                Component.literal(
                                                                                                                                                                                                "Disable durability at 100%."));

                                                                                                                                                return 1;
                                                                                                                                        })))

                                                                        // /etm config
                                                                        .then(
                                                                                        ClientCommands.literal(
                                                                                                        "config")
                                                                                                        .executes(context -> {

                                                                                                                Minecraft client = Minecraft
                                                                                                                                .getInstance();

                                                                                                                client.execute(() -> client.gui
                                                                                                                                .setScreen(
                                                                                                                                                ElytraConfigScreen
                                                                                                                                                                .create(
                                                                                                                                                                                client.gui.screen())));

                                                                                                                return 1;
                                                                                                        })));
                                });
        }

        private static void showHelp(
                        net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource source) {
                source.sendFeedback(
                                Component.literal(
                                                "Elytra Tracker Mod Commands:\n" +
                                                                "/etm help - Display this help message\n" +
                                                                "/etm hud on - Enable the HUD\n" +
                                                                "/etm hud off - Disable the HUD\n" +
                                                                "/etm config - Open the configuration menu\n" +
                                                                "/etm bar vanilla - Enable the Vanilla style bar\n" +
                                                                "/etm bar spectrum - Enable the spectrum Vanilla style bar\n"
                                                                +
                                                                "/etm durability percentage - Enable the percentage indicator\n"
                                                                +
                                                                "/etm durability remaining - Enable the remaining value indicator\n"
                                                                +
                                                                "/etm durability time - Enable the estimated remaining flight time indicator\n"
                                                                +
                                                                "/etm full on - Display durability indicator at 100%\n"
                                                                +
                                                                "/etm full off - Disable durability indicator at 100%"));
        }
}
