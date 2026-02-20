package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.Scelli;
import net.velli.scelli.ScelliUtil;
import net.velli.scelli.widget.widgets.TextDisplayWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.Widgets;
import net.velli.scelli.widget.widgets.containers.ContainerWidget;

import java.util.ArrayList;
import java.util.List;

public class ReportWidget extends ContainerWidget<ReportWidget> {

    protected ReportData data;
    protected final TextDisplayWidget textDisplay = Widgets.create(TextDisplayWidget::new);

    @Override
    public List<Widget<?>> getWidgets() {
        return List.of(textDisplay);
    }

    @Override
    public void renderMain(DrawContext context, int mouseX, int mouseY, float delta) {
        updateText();
        withDimensions(width(), textDisplay.height() + 6, true);
        textDisplay.withPosition(3, 3, true);
        context.fill(0, 0, width(), height(), 0x66000000);
        renderChildren(context, mouseX, mouseY);
    }

    @Override
    public ReportWidget getWidget() {
        return this;
    }

    public ReportWidget withData(ReportData data) {
        this.data = data;
        return getWidget();
    }

    protected void updateText() {
        MutableText text = Text.empty();
        text.append(data.title());
        text.append(Text.literal("").append(Text.literal("Offender: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.offender + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        text.append(Text.literal("").append(Text.literal("Offense: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.reason + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        text.append(Text.literal("").append(Text.literal("Location: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(data.location + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false))));
        ArrayList<OrderedText> lines = new ArrayList<>();
        for (Text line : ScelliUtil.splitTextNewline(text)) {
            lines.addAll(ModUtilities.MC.textRenderer.wrapLines(line, ModUtilities.MC.textRenderer.getWidth(data.title())));
        }
        textDisplay.setLines(lines);
        textDisplay.withDimensions(ModUtilities.MC.textRenderer.getWidth(data.title()), (Scelli.MC.textRenderer.fontHeight + 2) * lines.size(), true);
        withDimensions(ModUtilities.MC.textRenderer.getWidth(data.title()), (Scelli.MC.textRenderer.fontHeight + 2) * lines.size(), true);
    }

    @Override
    public void onClick(int mouseX, int mouseY) {
        super.onClick(mouseX, mouseY);
    }
}
