package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.screen.WidgetContainerScreen;
import net.velli.scelli.widget.widgets.Alignment;
import net.velli.scelli.widget.widgets.TextDisplayWidget;
import net.velli.scelli.widget.widgets.Widgets;
import net.velli.scelli.widget.widgets.containers.VerticalListWidget;

import java.util.List;

public class ReportScreen extends WidgetContainerScreen {

    protected VerticalListWidget reportList;

    public ReportScreen() {
        super(null);
        List<ReportData> reports = ReportOverlayFeature.instance.reports;
        reportList = Widgets.create(VerticalListWidget::new).withDimensions(300, reports.isEmpty() ? 30 : ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        reportList.withAlignment(Alignment.CENTER);
        reportList.addWidgets(Widgets.create(TextDisplayWidget::new, 0, 0, 50, 16));
        reportList.addWidgets(Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.literal(reports.isEmpty() ? "No reports this session!" : "Reports")).withTextAlignment(Alignment.CENTER));
        for (ReportData report : ReportOverlayFeature.instance.reports.reversed()) reportList.addWidgets(Widgets.create(ReportWidget::new).getWidget().withData(report));
        reportList.withPosition(0, 500, true);
        reportList.withPosition(0, 0, false);
        addWidgets(reportList);
    }

    @Override
    public void resize(int width, int height) {
        List<ReportData> reports = ReportOverlayFeature.instance.reports;
        reportList.withDimensions(300, reports.isEmpty() ? 30 : ModUtilities.MC.getWindow().getScaledHeight() - 50, true);
        super.resize(width, height);
    }
}
