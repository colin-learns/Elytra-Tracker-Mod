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
                                                            ClientCommands.literal("on")
                                                                    .executes(context -> {
                                                                        ElytraConfig.HANDLER
                                                                                .instance().hudEnabled = true;
                                                                        ElytraConfig.HANDLER.save();

                                                                        context.getSource().sendFeedback(
                                                                                Component.literal(
                                                                                        "Elytra Tracker HUD enabled."));

                                                                        return 1;
                                                                    }))

                                                    .then(
                                                            ClientCommands.literal("off")
                                                                    .executes(context -> {
                                                                        ElytraConfig.HANDLER
                                                                                .instance().hudEnabled = false;
                                                                        ElytraConfig.HANDLER.save();

                                                                        context.getSource().sendFeedback(
                                                                                Component.literal(
                                                                                        "Elytra Tracker HUD disabled."));

                                                                        return 1;
                                                                    })))

                                    // /etm config
                                    .then(
                                            ClientCommands.literal("config")
                                                    .executes(context -> {

                                                        Minecraft client = Minecraft.getInstance();

                                                        client.execute(() -> client.gui.setScreen(
                                                                ElytraConfigScreen.create(
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
                                "/etm config - Open the configuration menu"));
    }
}
