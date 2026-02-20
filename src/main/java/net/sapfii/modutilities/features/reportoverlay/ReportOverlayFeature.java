package net.sapfii.modutilities.features.reportoverlay;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.Feature;
import net.sapfii.modutilities.features.interfaces.KeyBindListeningFeature;
import net.sapfii.modutilities.features.interfaces.PacketListeningFeature;
import net.sapfii.modutilities.features.interfaces.RenderedFeature;
import net.sapfii.modutilities.features.vanishoverlay.VanishMode;
import net.sapfii.modutilities.keybinds.ModUtilsKeyBinds;
import net.sapfii.modutilities.sounds.ModUtilsSounds;
import net.velli.scelli.widget.interfaces.ClickableWidget;
import net.velli.scelli.widget.widgets.Widget;
import net.velli.scelli.widget.widgets.Widgets;
import org.joml.Vector2f;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportOverlayFeature extends Feature implements RenderedFeature, PacketListeningFeature, KeyBindListeningFeature {
    public ReportOverlayFeature() { instance = this; }
    private static final Pattern REPORT_REGEX = Pattern.compile(
            "! Incoming Report \\((.+)\\)\\n" +
            "\\| {2}Offender: (.+)\\n" +
            "\\| {2}Offense: (.+)\\n" +
            "\\| {2}Location: (.+) Mode (.+)"
    );

    public static ReportOverlayFeature instance;

    private final List<Widget<?>> widgets = new CopyOnWriteArrayList<>();

    public List<ReportData> reports = new CopyOnWriteArrayList<>();

    private static String queuedTeleport = "";
    private static String queuedNodeSwitch = "";

    @Override
    public PacketResult onPacket(Packet<?> packet) {
        if (!ModUtilsConfig.config.useReportDisplay.get()) return PacketResult.PASS;
        if (packet instanceof GameStateChangeS2CPacket && !queuedTeleport.isEmpty() && queuedNodeSwitch.isEmpty()) {
            ModUtilities.sendCommand(queuedTeleport);
            queuedTeleport = "";
        }
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketResult.PASS;
        String string = msgText.getString();

        if (string.matches("You are already connected to this server!") && !queuedTeleport.isEmpty()) {
            ModUtilities.sendCommand(queuedTeleport);
            queuedTeleport = "";
            return PacketResult.CANCEL;
        }

        if (string.matches("» Vanish enabled. You will not be visible to other players.") && !queuedNodeSwitch.isEmpty()) {
            ModUtilities.sendCommand(queuedNodeSwitch);
            queuedNodeSwitch = "";
        }

        Matcher matcher = REPORT_REGEX.matcher(string);
        if (matcher.find()) {
            ReportData newReport = ReportData.create(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4)
            );
            ReportWidget widget = Widgets.create(ReportWidget::new).getWidget().withData(newReport);
            widget.withOpacity(1, true);
            widget.withOpacity(255, false);
            widgets.addFirst(widget);
            reports.add(newReport);
            return PacketResult.CANCEL;
        }

        return PacketResult.PASS;
    }

    @Override
    public void renderMain(DrawContext context, int mouseX, int mouseY, float delta) {
        withDimensions(10000, 100000, true);
//        System.out.println("is this even rendering");
        if (getWidgets().isEmpty()) return;
        if (getWidgets().getFirst().opacity() == 0) getWidgets().removeFirst();
        int x = 0;
        for (Widget<?> widget : getWidgets()) {
            if (!(widget instanceof ReportWidget rw)) continue;
            rw.withPosition(x, 20, false);
            rw.updateText();
            rw.withDimensions(ModUtilities.MC.textRenderer.getWidth(rw.data.title()), rw.textDisplay.height() + 6, true);
            if (rw != getWidgets().getFirst()) rw.withOpacity(1, false);
            else if (rw.opacity() != 0) rw.withOpacity(255, false);
            if (getWidgets().getFirst().opacity() == 0 && getWidgets().size() > 1 && getWidgets().get(1) == rw) rw.withOpacity(255, false);
            if (rw.opacity() != 0) x += rw.width() + 4;
        }
        if (ModUtilsConfig.config.useReportDisplay.get()) renderChildren(context, mouseX, mouseY);
    }

    @Override
    public List<Widget<?>> getWidgets() {
        return widgets;
    }

    @Override
    public ReportOverlayFeature getWidget() {
        return this;
    }

    @Override
    public void onKeyBindPress(KeyBinding keyBind) {
        if (keyBind.equals(ModUtilsKeyBinds.DISMISS_REPORT) && !getWidgets().isEmpty()) {
            getWidgets().removeFirst();
            ModUtilities.playSound(ModUtilsSounds.REPORT_DISMISS, 1f);
        }
    }

    public static void clickedReport(ReportData data) {
        if (!(ModUtilities.MC.currentScreen instanceof ChatScreen || ModUtilities.MC.currentScreen instanceof ReportScreen)) return;
        String serverCmd = "server " + data.location.toLowerCase().replace(" ", "");
        if (serverCmd.contains("private")) serverCmd = serverCmd.replace("node", "");
        if (VanishMode.is(VanishMode.NONE)) {
            ModUtilities.sendCommand("mod v");
            queuedNodeSwitch = serverCmd;
        } else {
            ModUtilities.sendCommand(serverCmd);
        }
        queuedTeleport = "tp " + data.offender;
    }
}
