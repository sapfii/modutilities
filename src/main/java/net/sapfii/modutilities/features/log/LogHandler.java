package net.sapfii.modutilities.features.log;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.features.log.screen.LogScreen;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import net.sapfii.modutilities.packet.send.ClientCommandListener;
import net.sapfii.modutilities.packet.send.CommandListenerRegistry;
import net.sapfii.sapscreens.ScreenHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogHandler implements ClientPacketListener, ClientCommandListener {
    public static LogHandler handler = new LogHandler();

    private static final Pattern LOG_REGEX = Pattern.compile("--------------\\[ (.+) Log | (.+) ]--------------");
    private static Text logHeader = Text.literal("");
    private static boolean collectingLogs = false;
    private static boolean awaitingLogs = false;
    public static String lastLogCmd = "";

    private ArrayList<Text> logLines = new ArrayList<>();

    public static void init() {
        PacketListenerRegistry.register(handler);
        CommandListenerRegistry.register(handler);
    }

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketEventResult.PASS;
        if (!ModUtilsConfig.config.useLogScreen) return PacketEventResult.PASS;
        String string = msgText.getString();

        Matcher matcher = LOG_REGEX.matcher(string);
        if (matcher.find()) {
            if (collectingLogs) {
                ScreenHandler.openScreen(new LogScreen(ModUtilities.MC.currentScreen, logLines, logHeader));
                logHeader = Text.literal("");
                logLines = new ArrayList<>();
            } else if (awaitingLogs) {
                logHeader = msgText;
                awaitingLogs = false;
            }
            collectingLogs = !collectingLogs;
            return PacketEventResult.CANCEL;
        }

        if (collectingLogs) {
            logLines.add(msgText);
            return PacketEventResult.CANCEL;
        }
        return PacketEventResult.PASS;
    }

    @Override
    public CommandEventResult onCommand(String command) {
        if (command.startsWith("mod log") || command.startsWith("admin log")) {
            awaitingLogs = true;
            lastLogCmd = command;
            return CommandEventResult.PASS;
        }
        return CommandEventResult.PASS;
    }
}
