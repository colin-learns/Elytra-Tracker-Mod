
package com.colin.elytratrackermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.HumanoidArm;

public class ElytraTrackerModClient implements ClientModInitializer {

    // HudElementRegistry was changed to use .attachElementAfter to add zoom handling.  
    // Vanilla HUD dimming is also being applied to the mod HUD via this change.
    @Override
    public void onInitializeClient() {
        ElytraConfig.HANDLER.load();
        ElytraTrackerCommands.register();
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.HOTBAR,
                Identifier.fromNamespaceAndPath(
                        "elytratrackermod",
                        "tracker"),
                ElytraTrackerModClient::render);
    }

    // Code block for blending percentage durability text colors
    private static int blendColors(int color1, int color2, float ratio) {
        ratio = Math.max(0.0f, Math.min(1.0f, ratio));

        int a1 = (color1 >>> 24) & 0xFF;
        int r1 = (color1 >>> 16) & 0xFF;
        int g1 = (color1 >>> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int a2 = (color2 >>> 24) & 0xFF;
        int r2 = (color2 >>> 16) & 0xFF;
        int g2 = (color2 >>> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int a = Math.round(a1 + (a2 - a1) * ratio);
        int r = Math.round(r1 + (r2 - r1) * ratio);
        int g = Math.round(g1 + (g2 - g1) * ratio);
        int b = Math.round(b1 + (b2 - b1) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static void render(
            GuiGraphicsExtractor graphics,
            net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null) {
            return;
        }

        if (!ElytraConfig.HANDLER.instance().hudEnabled) {
            return;
        }

        ItemStack elytra = client.player.getItemBySlot(
                EquipmentSlot.CHEST);

        if (elytra.isEmpty() || !elytra.is(Items.ELYTRA)) {
            return;
        }

        // Calculate remaining elytra durability.
        int maximum = elytra.getMaxDamage();
        int remaining = maximum - elytra.getDamageValue();

        int percentage = maximum > 0
                ? (int) Math.ceil(remaining * 100.0 / maximum)
                : 100;

        // Hotbar slot, x,y for upper left corner. guiWidth =512 guiHeight = 274.
        // Multiply by 5 to get actual pixel numbers.
        int boxX = client.options.mainHand().get() == HumanoidArm.RIGHT
                ? graphics.guiWidth() / 2 + 91
                : graphics.guiWidth() / 2 - 127;
        int boxY = graphics.guiHeight() - 23;

        // Hotbar sprite, uses the right offhand.  
        // Not sure if offhand_left is different at all.
        Identifier slotSprite = Identifier.fromNamespaceAndPath(
                "minecraft",
                "hud/hotbar_offhand_right");

        // Defines the size of the hotbar sprite, which is 29 X 24 GUI units (145X120
        // pixels)
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                slotSprite,
                boxX,
                boxY,
                29,
                24
        );

        // Elytra icon size and position.
        int iconSize = 16;
        int iconX = boxX + (37 - iconSize) / 2;
        int iconY = boxY + (24 - iconSize) / 2;
        int color;
        if (percentage >= 50) {
            color = blendColors(0xFF69FF00, 0xFFFFF600,
                    (100 - percentage) / 50.0f);
        } else if (percentage >= 40) {
            color = blendColors(0xFFFFF600, 0xFFFF8300,
                    (50 - percentage) / 10.0f);
        } else if (percentage >= 20) {
            color = blendColors(0xFFFF8300, 0xFFFF4500,
                    (40 - percentage) / 20.0f);
        } else {
            color = 0xFFFF4500;
        }

        graphics.item(elytra, iconX, iconY);

        // Determine whether to display the durability indicator.
        if (ElytraConfig.HANDLER.instance().showDurabilityAtFull
                || elytra.getDamageValue() > 0) {

            // Choose between percentage, remaining, and durability bar.
            if (ElytraConfig.HANDLER.instance().durabilityDisplay == ElytraConfig.DurabilityDisplay.PERCENTAGE) {

                String text = percentage + "%";
                float fontScale = 0.65f;

                graphics.pose().pushMatrix();

                graphics.pose().translate(
                        boxX + 29 / 2.0f + 4,
                        boxY + 16);

                graphics.pose().scale(fontScale, fontScale);

                graphics.centeredText(
                        client.font,
                        text,
                        0,
                        0,
                        color);

                graphics.pose().popMatrix();

            } else if (ElytraConfig.HANDLER.instance().durabilityDisplay == ElytraConfig.DurabilityDisplay.REMAINING) {

                String remainingText = String.valueOf(remaining);
                float fontScale = 0.65f;

                graphics.pose().pushMatrix();

                graphics.pose().translate(
                        boxX + 29 / 2.0f + 4,
                        boxY + 16);

                graphics.pose().scale(fontScale, fontScale);

                graphics.centeredText(
                        client.font,
                        remainingText,
                        0,
                        0,
                        color);

                graphics.pose().popMatrix();

            } else {

                // Draw the durability bar background.
                int barX = iconX + 2;
                int barY = iconY + 13;

                int barWidth = Math.max(
                        0,
                        Math.min(13, elytra.getBarWidth()));

graphics.fill(
        barX,
        barY,
        barX + 13,
        barY + 2,
        0xFF000000);


                if (ElytraConfig.HANDLER.instance().barColorMode == ElytraConfig.BarColorMode.VANILLA) {

                    // Vanilla color behavior.
                    int barColor = elytra.getBarColor();

                    if (barWidth > 0) {
                        graphics.fill(
                                barX,
                                barY,
                                barX + barWidth,
                                barY + 1,
                                0xFF000000  | (barColor & 0x00FFFFFF));
                    }

                } else {

                    // Spectrum mode: red on the left, violet on the right.
                    for (int i = 0; i < barWidth; i++) {

                        float hue = (i / 12.0f) * 0.78f;

                        int spectrumColor = java.awt.Color.HSBtoRGB(
                                hue,
                                1.0f,
                                1.0f);

                        graphics.fill(
                                barX + i,
                                barY,
                                barX + i + 1,
                                barY + 1,
                                0xFF000000  | (spectrumColor & 0x00FFFFFF));
                    }
                }
            }
        }
    }
}
