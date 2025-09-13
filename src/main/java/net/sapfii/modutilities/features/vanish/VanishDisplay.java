package net.sapfii.modutilities.features.vanish;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;

public class VanishDisplay {

    private static final Text ADMINVTEXT = Text.literal("Admin").withColor(0xFF0000).append(Text.literal(" Vanish").withColor(0xFFFFFF));
    private static final Text MODVTEXT = Text.literal("Mod").withColor(0x00AA00).append(Text.literal(" Vanish").withColor(0xFFFFFF));

    private static final int offsetX = 10;
    private static final int baseOffsetY = 5;
    private static int offsetY = baseOffsetY;
    private static float subpixelOffsetY = baseOffsetY;
    private static final int padding = 3;
    private static int bgColor = 0x88000000;

    private static double lastRender = 0;


    private static Text text = Text.literal("");

    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        float delta = (float) (System.currentTimeMillis() - lastRender) / 1000;
        if (!ModUtilsConfig.config.useVanishDisplay) {
            return;
        }
        if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.ADMIN) {
            text = ADMINVTEXT;
            bgColor = 0x88FF0000;
        } else if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.MOD) {
            text = MODVTEXT;
            bgColor = 0x8800AA00;
        }

        if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.NONE) {
            subpixelOffsetY = ModUtilities.lerp(subpixelOffsetY, -ModUtilities.MC.textRenderer.fontHeight - baseOffsetY - padding * 2, 8F * delta);
        } else {
            subpixelOffsetY = ModUtilities.lerp(subpixelOffsetY, baseOffsetY, 8F * delta);
        }
        offsetY = Math.round(subpixelOffsetY);

        int bgX1 = offsetX;
        int bgX2 = bgX1 + ModUtilities.MC.textRenderer.getWidth(text) + padding * 2;
        int bgY1 = 0;
        int bgY2 = ModUtilities.MC.textRenderer.fontHeight + offsetY + padding * 2;

        drawContext.fill(bgX1, bgY1, bgX2, bgY2, bgColor);
        drawContext.drawTextWithShadow(ModUtilities.MC.textRenderer, text, bgX1 + padding, bgY1 + padding + offsetY, 0xFFFFFFFF);
        lastRender = System.currentTimeMillis();
    }
}
