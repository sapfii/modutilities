package net.sapfii.modutilities.features.log.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.sapscreens.screens.widgets.TextDisplayWidget;
import net.sapfii.sapscreens.screens.widgets.Widget;
import net.sapfii.sapscreens.screens.widgets.WidgetContainerScreen;
import net.sapfii.sapscreens.screens.widgets.WidgetListBox;

import java.util.ArrayList;
import java.util.List;

public class LogScreen extends WidgetContainerScreen {
    private boolean scrollDown = false;
    private WidgetListBox logBox = new WidgetListBox().withPosition(25, 35).centerOnParent(true, true).withPadding(15, 50, 2);
    public LogScreen(Screen previousScreen, List<Text> logLines, Text header) {
        super(previousScreen);
        if (ModUtilsConfig.config.logDirection == ModUtilsConfig.LOG_DIRECTION.UP) logLines = logLines.reversed();
        List<Widget<?>> lines = new ArrayList<>();
        lines.add(new TextDisplayWidget(header, 2, TextDisplayWidget.Alignment.CENTER));
        lines.add(new TextDisplayWidget(Text.literal(" "), 2, TextDisplayWidget.Alignment.CENTER));
        for (Text line : logLines) {
            lines.add(new TextDisplayWidget(line, 2, TextDisplayWidget.Alignment.LEFT));
        }
        lines.add(new TextDisplayWidget(Text.literal(" "), 2, TextDisplayWidget.Alignment.CENTER));
        lines.add(new TextDisplayWidget(header, 2, TextDisplayWidget.Alignment.CENTER));
        if (ModUtilsConfig.config.logDirection == ModUtilsConfig.LOG_DIRECTION.DOWN) scrollDown = true;
        logBox.addWidgets(lines);
        addWidget(logBox);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (scrollDown) logBox.setScrollAmount(logBox.getMaxScrollAmount());
        scrollDown = false;
    }

    @Override
    public void removeWidget(Widget widget) {
        elements.remove(widget);
    }
}
