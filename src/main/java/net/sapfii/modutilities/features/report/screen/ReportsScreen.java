package net.sapfii.modutilities.features.report.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.sapfii.modutilities.features.report.ReportData;
import net.sapfii.modutilities.features.report.ReportHandler;
import net.sapfii.sapscreens.screens.widgets.TextDisplayWidget;
import net.sapfii.sapscreens.screens.widgets.Widget;
import net.sapfii.sapscreens.screens.widgets.WidgetContainerScreen;
import net.sapfii.sapscreens.screens.widgets.WidgetListBox;
import net.sapfii.sapscreens.screens.widgets.interfaces.WidgetContainer;

import java.util.ArrayList;

public class ReportsScreen extends WidgetContainerScreen {
    private WidgetListBox reportsBox = new WidgetListBox().withPosition(75, 25).centerOnParent(true, true).withPadding(10, 100, 2);


    public ReportsScreen(Screen previousScreen) {
        super(previousScreen);
        addWidget(reportsBox);
        refresh();
    }

    public void refresh() {
        clearChildren();
        addWidget(reportsBox);
        reportsBox.clearChildren();
        if (!ReportHandler.sessionReports.isEmpty()) {
            reportsBox.addWidget(new TextDisplayWidget(Text.literal("Reports"), 2, TextDisplayWidget.Alignment.CENTER));
            reportsBox.addWidget(new TextDisplayWidget(Text.literal(" "), 2, TextDisplayWidget.Alignment.CENTER));
            for (ReportData report : ReportHandler.sessionReports.reversed()) {
                report.updateTime();
                reportsBox.addWidget(new TextDisplayWidget(report.title, 2, TextDisplayWidget.Alignment.LEFT));
                reportsBox.addWidget(new TextDisplayWidget(report.text, 2, TextDisplayWidget.Alignment.LEFT));
                reportsBox.addWidget(new TextDisplayWidget(Text.literal(" "), 2, TextDisplayWidget.Alignment.CENTER));
            }
        } else {
            addWidget(new TextDisplayWidget(Text.literal("No reports this session!"), 2, TextDisplayWidget.Alignment.CENTER).withDimensions(100, 0).centerOnParent(true, true));
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        refresh();
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void removeWidget(Widget widget) {
        elements.remove(widget);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
