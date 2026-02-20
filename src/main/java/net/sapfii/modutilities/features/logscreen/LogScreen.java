package net.sapfii.modutilities.features.logscreen;

import net.minecraft.client.MinecraftClient;
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

public class LogScreen extends WidgetContainerScreen {
    protected VerticalListWidget list;

    public LogScreen(List<Text> lines, Text header) {
        super(null);
        list = Widgets.create(VerticalListWidget::new)
                .withAlignment(Alignment.CENTER)
                .withDimensions(
                        ModUtilities.MC.getWindow().getScaledWidth() - 150,
                        ModUtilities.MC.getWindow().getScaledHeight() - 50,
                        true);
        if (ModUtilsConfig.config.logDirection.is(LogDirection.UP)) lines = lines.reversed();
        List<Widget<?>> screenLines = new ArrayList<>(List.of(
                Widgets.create(TextDisplayWidget::new).setLines(header).withAlignment(Alignment.CENTER),
                Widgets.create(TextDisplayWidget::new)
        ));
        lines.forEach(line -> screenLines.add(Widgets.create(TextDisplayWidget::new).setLines(line)));
        screenLines.add(Widgets.create(TextDisplayWidget::new));
        screenLines.add(Widgets.create(TextDisplayWidget::new).setLines(header).withAlignment(Alignment.CENTER));
        screenLines.forEach(line -> list.addWidgets(line));
        list.withPosition(0, 500, true);
        list.withPosition(0, 0, false);
        addWidgets(list);
    }

    @Override
    public void resize(int width, int height) {
        list.withDimensions(ModUtilities.MC.getWindow().getScaledWidth() - 150, ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        super.resize(width, height);
    }
}
