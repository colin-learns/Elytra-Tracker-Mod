
package com.colin.elytratrackermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
// import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;

public class ElytraTrackerModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(
                "elytratrackermod",
                "tracker"
            ),
            ElytraTrackerModClient::render
        );
    }

    // Test change 2
    // Code block for blending durability text colors
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
        net.minecraft.client.DeltaTracker deltaTracker
    ) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null) {
            return;
        }

        // Code block creates a variable that can be used in other code where the tracker needs to dim.  Covers containers, trading, workstations as well as book viewing and editing.
        boolean shouldDim =
         client.gui.screen() instanceof AbstractContainerScreen<?>
         || client.gui.screen() instanceof BookEditScreen
         || client.gui.screen() instanceof BookViewScreen;

        ItemStack elytra = client.player.getItemBySlot(
            EquipmentSlot.CHEST
        );

        if (elytra.isEmpty() || !elytra.is(Items.ELYTRA)) {
            return;
        }

        // Calculate remaining durability.
        int maximum = elytra.getMaxDamage();
        int remaining = maximum - elytra.getDamageValue();

        System.out.println(
        "Elytra damage: " + elytra.getDamageValue()
);

        int percentage = maximum > 0
            ? (int) Math.ceil(remaining * 100.0 / maximum)
            : 100;

        // Hotbar slot, x,y for upper left corner.  guiWidth =512 guiHeight = 274.  Multiply by 5 to get actual pixel numbers.
        int boxX = graphics.guiWidth() / 2 + 91;
        int boxY = graphics.guiHeight() - 23;

        // Hotbar sprite, uses the right offhand.  Mostly relevant because the sprite is bigger than it looks (and in a way that is different from offhand left), forcing the dimming overlay to be a custom size.
        Identifier slotSprite = Identifier.fromNamespaceAndPath(
            "minecraft",
            "hud/hotbar_offhand_right"
        );

        // Defines the size of the hotbar sprite, which is 29 X 24 GUI units (145X120 pixels)
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            slotSprite,
            boxX,
            boxY,
            29,
            24
            // slotAlpha
        );

        // Preserve your current Elytra icon position.
        int iconSize = 16;
        int iconX = boxX + (37 - iconSize) / 2;
        int iconY = boxY + (24 - iconSize) / 2;

        // Creates overlay for dimming the hotbar slot and elytra.  Not ideal, but haven't found a way to adjust the alpha of the elytra sprite
        graphics.item(elytra, iconX, iconY);
        if (shouldDim) {

            int overlayX = boxX + 8;
            int overlayY = boxY + 2;
            int overlayWidth = 20;
            int overlayHeight = 20;

            graphics.fill(
                overlayX,
                overlayY,
                overlayX + overlayWidth,
                overlayY + overlayHeight,
                0xB6000000
        );        
        }
        
        // Defines text color changes as the elytra durability goes down
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
            color = blendColors(0xFFFF4500, 0xFFFF4500, 0);
        }

        // Reduce text opacity while inventory is open.
        if (shouldDim) {
            color = (72 << 24) | (color & 0x00FFFFFF);
        }

        // Preserve your current percentage position and size.
        String text = percentage + "%";
        float fontScale = 0.65f;

        graphics.pose().pushMatrix();

        graphics.pose().translate(
            boxX + 29 / 2.0f + 4,
            boxY + 16
        );

        graphics.pose().scale(fontScale, fontScale);

        graphics.centeredText(
            client.font,
            text,
            0,
            0,
            color
        );

        graphics.pose().popMatrix();
    }
}

