package net.sapfii.modutilities.features.report;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.report.screen.ReportsScreen;
import net.sapfii.modutilities.keybinds.KeyBindListener;
import net.sapfii.modutilities.keybinds.KeyBindRegistry;
import net.sapfii.modutilities.keybinds.ModUtilsKeyBinds;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import net.sapfii.modutilities.sounds.ModUtilsSounds;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportHandler implements ClientPacketListener, KeyBindListener {
    private static final Identifier REPORT_DISPLAY_LAYER = Identifier.of("modutils", "report-display-layer");
    private static final Pattern REPORT_REGEX = Pattern.compile("! Incoming Report \\((.+)\\)\\n\\| {2}Offender: (.+)\\n\\| {2}Offense: (.+)\\n\\| {2}Location: (.+) Mode (.+)");

    public static ReportHandler handler = new ReportHandler();
    public static CopyOnWriteArrayList<ReportData> reports = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<ReportData> sessionReports = new CopyOnWriteArrayList<>();

    private static double lastDismiss = System.currentTimeMillis();

    public static void init() {
        HudRenderCallback.EVENT.register(REPORT_DISPLAY_LAYER, ReportHandler::render);
        PacketListenerRegistry.register(handler);
        KeyBindRegistry.registerListener(handler);
    }

    private static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (!ModUtilsConfig.config.useReportDisplay) return;
        int width = 0;
        for (ReportData report : reports.reversed()) {
            width = report.render(drawContext, width);
            if (report.textOpacity <= 100F && report.beingRemoved) {
                reports.remove(report);
            }
        }
    }

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketEventResult.PASS;
        String string = msgText.getString();
        Matcher matcher = REPORT_REGEX.matcher(string);
        if (matcher.find()) {
            ReportData report = new ReportData(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4), matcher.group(5)
            );
            reports.add(report);
            sessionReports.add(report);
            if (ModUtilities.MC.currentScreen instanceof ReportsScreen screen) {
                screen.refresh();
            }
            return PacketEventResult.CANCEL;
        }
        return PacketEventResult.PASS;
    }

    @Override
    public void keyPressed(KeyBinding keyBinding) {
        if (keyBinding.equals(ModUtilsKeyBinds.DISMISS_REPORT)) {
            if (System.currentTimeMillis() - lastDismiss < 500 || reports.isEmpty() || reports.getLast().beingRemoved || !ModUtilsConfig.config.useReportDisplay) return;
            if (Screen.hasControlDown()) reports.forEach(report -> report.beingRemoved = true);
            else reports.getLast().beingRemoved = true;
            ModUtilities.playSound(ModUtilsSounds.REPORT_DISMISS, 1F);
            lastDismiss = System.currentTimeMillis();
        }
    }
}
