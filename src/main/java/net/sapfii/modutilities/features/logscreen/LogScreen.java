package net.sapfii.modutilities.features.logscreen;

import com.terraformersmc.modmenu.util.mod.Mod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.config.enums.LogDirection;
import net.velli.scelli.screen.WidgetContainerScreen;
import net.velli.scelli.widget.widgets.Alignment;
import net.velli.scelli.widget.widgets.TextDisplayWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.Widgets;
import net.velli.scelli.widget.widgets.containers.VerticalListWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogScreen extends WidgetContainerScreen {
    protected VerticalListWidget list;
    protected List<Text> lines;
    protected Text header;

    public LogScreen(List<Text> lines, Text header) {
        super(null);
        if (ModUtilsConfig.config.logDirection.is(LogDirection.UP)) lines = lines.reversed();
        this.lines = lines;
        this.header = header;
        list = Widgets.create(VerticalListWidget::new)
                .withAlignment(Alignment.CENTER)
                .withDimensions(
                        ModUtilities.MC.getWindow().getScaledWidth() - 150,
                        ModUtilities.MC.getWindow().getScaledHeight() - 50,
                        true)
                .withPadding(2, 10,2, 2);
        list.withPosition(0, 500, true);
        list.withPosition(0, 0, false);
    }

    @Override
    public void resize(int width, int height) {
        list.withDimensions(ModUtilities.MC.getWindow().getScaledWidth() - 150, ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        super.resize(width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        updateText();
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    public void updateText() {
        list.clearWidgets();
        list.reversed = false;
        if (ModUtilsConfig.config.logDirection.is(LogDirection.DOWN)) {
            list.reversed = true;
        }
        List<Widget<?>> screenLines = new ArrayList<>(List.of(
                Widgets.create(TextDisplayWidget::new, 0, 0, 300, 9).setLines(header).withTextAlignment(Alignment.CENTER),
                Widgets.create(TextDisplayWidget::new)
        ));
        for (Text line : lines) {
            for (OrderedText orderedLine : getTextRenderer().wrapLines(line, 300)) {
                screenLines.add(Widgets.create(TextDisplayWidget::new, 0, 0, 300, 9).setLines(orderedLine));
            }
        }
//        lines.forEach(line -> screenLines.add(Widgets.create(TextDisplayWidget::new, 0, 0, 300, 9).setLines(line)));
        screenLines.add(Widgets.create(TextDisplayWidget::new));
        screenLines.add(Widgets.create(TextDisplayWidget::new, 0, 0, 300, 9).setLines(header).withTextAlignment(Alignment.CENTER));
        screenLines.forEach(line -> list.addWidgets(line));
        addWidgets(list);
    }
}
