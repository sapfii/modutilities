package net.sapfii.modutilities.features.vanishoverlay;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.Scelli;
import net.velli.scelli.widget.widgets.TextDisplayWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.Widgets;
import net.velli.scelli.widget.widgets.containers.ContainerWidget;

import java.util.List;

public class VanishWidget extends ContainerWidget<VanishWidget> {

    protected final TextDisplayWidget textDisplay = Widgets.create(TextDisplayWidget::new);

    public static VanishWidget create() {
        return new VanishWidget();
    }

    private static Text vanishText = Text.empty();
    private static int bgColor = 0x66000000;

    @Override
    public void renderMain(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = ModUtilities.MC.textRenderer;
        if (VanishMode.is(VanishMode.ADMIN)) vanishText = Text.literal("Admin").withColor(0xFF0000).append(Text.literal(" Vanish"));
        if (VanishMode.is(VanishMode.ADMIN)) bgColor = 0x66DD0023;
        if (VanishMode.is(VanishMode.MOD)) vanishText = Text.literal("Mod").withColor(0x00FF00).append(Text.literal(" Vanish"));
        if (VanishMode.is(VanishMode.MOD)) bgColor = 0x6623DD00;
        textDisplay.withPosition(2, 5, true);
        textDisplay.withDimensions(width(), 16, true);
        textDisplay.setLines(vanishText);
        withDimensions(textRenderer.getWidth(vanishText) + 4, 16, true);
        context.fill(0, 0, width(), height(), bgColor);
        renderChildren(context, mouseX, mouseY);
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(textDisplay);
    }

    @Override
    public VanishWidget getWidget() {
        return this;
    }
}
