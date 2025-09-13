package net.sapfii.modutilities.features.setrank;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import net.sapfii.modutilities.packet.send.ClientCommandListener;
import net.sapfii.modutilities.packet.send.CommandListenerRegistry;
import net.sapfii.sapscreens.ScreenHandler;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetRankAutocomplete implements ClientCommandListener, ClientPacketListener {
    private static final Pattern CODE = Pattern.compile("Attempting to execute \"(.+)\". To verify, run this command again and add \\[HOVER] to the end\\. This key will expire in 1 minute\\.");

    public static final SetRankAutocomplete handler = new SetRankAutocomplete();
    private static boolean awaitingCode = false;
    private static String lastSetRankCommand = "";

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketEventResult.PASS;
        String string = msgText.getString();
        if (string.matches("An unexpected error occurred trying to execute that command")) awaitingCode = false;
        if (!awaitingCode) return PacketEventResult.PASS;
        Matcher matcher = CODE.matcher(string);
        if (matcher.find()) {
            List<Text> siblings = msgText.getSiblings();
            for (Text sibling : siblings) {
                if (sibling.getStyle().getHoverEvent() instanceof HoverEvent.ShowText(Text value)) {
                    String code = Objects.requireNonNull(value.getString());
                    ScreenHandler.openScreen(new ChatScreen("/" + lastSetRankCommand + " " + code));
                }
            }
        }
        awaitingCode = false;
        return PacketEventResult.PASS;
    }

    @Override
    public CommandEventResult onCommand(String command) {
        if (command.startsWith("setrank")) {
            awaitingCode = true;
            lastSetRankCommand = command;
        }
        return CommandEventResult.PASS;
    }

    public static void init() {
        CommandListenerRegistry.register(handler);
        PacketListenerRegistry.register(handler);
    }
}
