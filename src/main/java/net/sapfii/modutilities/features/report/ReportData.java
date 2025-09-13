package net.sapfii.modutilities.features.report;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.sapscreens.SapScreens;

import java.util.List;

public class ReportData {
    public String reporter, offender, offense, node, mode;
    public Text title, text;
    public boolean beingRemoved = false;
    private int mins = 0;
    private int secs = 0;
    private float secsFloat = 0F;
    private float x;
    public float textOpacity = 255F;
    private float bgOpacity = 136F;

    private double lastRender;
    private final int linePadding = ModUtilities.MC.textRenderer.fontHeight + 1;

    public ReportData(String reporter, String offender, String offense, String node, String mode) {
        lastRender = System.currentTimeMillis();
        this.reporter = reporter;
        this.offender = offender;
        this.offense = offense;
        this.node = node;
        this.mode = mode;

        updateText();
        List<Text> texts = SapScreens.splitTextNewline(text);
        int width = ModUtilities.MC.textRenderer.getWidth(title);
        for (Text txt : texts) {
            width = Math.max(width, ModUtilities.MC.textRenderer.getWidth(txt));
        }
        x = -width + 15;
    }

    private void updateText() {
        if (ModUtilsConfig.config.reportType == ModUtilsConfig.REPORT_TYPE.CLASSIC) {
            this.title = Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true))
                    .append(Text.literal("Incoming Report ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal("(" + reporter + ") ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + mins + "m" + secs + "s ago)").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        } else if (ModUtilsConfig.config.reportType == ModUtilsConfig.REPORT_TYPE.REIMAGINED) {
            this.title = Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true))
                    .append(Text.literal("Incoming Report ").styled(style -> style.withColor(0xFFAAAA).withBold(false)))
                    .append(Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true)))
                    .append(Text.literal("(").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal(reporter).styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal(") ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + mins + "m" + secs + "s ago)").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        } else if (ModUtilsConfig.config.reportType == ModUtilsConfig.REPORT_TYPE.REVAMPED) {
            this.title = Text.literal("[ ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false))
                    .append(Text.literal("Report ").styled(style -> style.withColor(0xFFAAAA).withBold(false)))
                    .append(Text.literal("filed by ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                    .append(Text.literal(reporter).styled(style -> style.withColor(Formatting.RED).withBold(false)))
                    .append(Text.literal(" ] ").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                    .append(Text.literal("(" + mins + "m" + secs + "s ago)").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        }

        List<OrderedText> offenseLines = ModUtilities.MC.textRenderer.wrapLines(StringVisitable.plain(offense), 200);
        MutableText mutableText = Text.literal("").append(Text.literal("Offender: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(offender + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)))
                .append(Text.literal("Offense: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)));

        for (OrderedText line : offenseLines) {
            StringBuilder sb = new StringBuilder();
            line.accept((index, style, codePoint) -> {
                sb.appendCodePoint(codePoint);
                return true;
            });
            mutableText.append(Text.literal(sb + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        }

        mutableText.append(Text.literal("Location: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                .append(Text.literal(node + " Mode " + mode).styled(style -> style.withColor(Formatting.WHITE).withBold(false)));
        this.text = mutableText;
        if (!ModUtilsConfig.config.useReportDisplay) {
            ModUtilities.sendMessage(
                    Text.literal("").append(Text.literal("! ").styled(style -> style.withColor(Formatting.RED).withBold(true)))
                            .append(Text.literal("Incoming Report ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                            .append(Text.literal("(" + reporter + ")\n").styled(style -> style.withColor(Formatting.DARK_GRAY).withBold(false)))
                            .append(Text.literal("Offender: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                            .append(Text.literal(offender + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)))
                            .append(Text.literal("Offense: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                            .append(Text.literal(offense + "\n").styled(style -> style.withColor(Formatting.WHITE).withBold(false)))
                            .append(Text.literal("Location: ").styled(style -> style.withColor(Formatting.GRAY).withBold(false)))
                            .append(Text.literal(node + " Mode " + mode).styled(style -> style.withColor(Formatting.WHITE).withBold(false))),
                    false);
        }
    }

    public void updateTime() {
        float delta = (float) (System.currentTimeMillis() - lastRender) / 1000;
        secsFloat += delta;
        int totalSecs = (int) secsFloat;
        mins = totalSecs / 60;
        secs = totalSecs % 60;
        updateText();
        lastRender = System.currentTimeMillis();
    }

    public int render(DrawContext drawContext, int xOffset) {
        float delta = (float) (System.currentTimeMillis() - lastRender) / 1000;

        float targetOpacity = 255F;
        float targetBgOpacity = 136F;
        if (xOffset > 1 || beingRemoved) {
            targetOpacity = 4F;
            targetBgOpacity = 0F;
        }
        textOpacity = ModUtilities.lerp(textOpacity, targetOpacity, 8F * delta);
        bgOpacity = ModUtilities.lerp(bgOpacity, targetBgOpacity, 8F * delta);

        updateTime();
        List<Text> texts = SapScreens.splitTextNewline(text);
        int height = 35 + linePadding + (texts.size() * linePadding);
        int width = ModUtilities.MC.textRenderer.getWidth(title);
        for (Text txt : texts) {
            width = Math.max(width, ModUtilities.MC.textRenderer.getWidth(txt));
        }

        x = ModUtilities.lerp(x, (float) xOffset, 8F * delta);
        int intX = (int) x;
        width += intX + 10;
        drawContext.fill(intX, 25, width, height, ((int) bgOpacity << 24));
        drawContext.drawText(ModUtilities.MC.textRenderer, title, 5 + intX, 30, ((int) textOpacity << 24) | 0x00FFFFFF, true);
        for (Text txt : texts) {
            drawContext.drawText(
                    ModUtilities.MC.textRenderer, txt,
                    5 + intX, 30 + linePadding + texts.indexOf(txt) * linePadding,
                    ((int) textOpacity << 24) | 0x00FFFFFF, true
            );
        }

        lastRender = System.currentTimeMillis();
        return width + 5;
    }
}
