package net.sapfii.modutilities.features.servermute;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerMessageMuter implements ClientPacketListener {
    private static final Pattern FAILEDSPEECH = Pattern.compile("(?<player>.+) tried to speak, but is muted\\.");
    private static final Pattern FAILEDJOIN = Pattern.compile("(?<player>.+) tried to join, but is banned \\((?<time>.+)\\)!");
    private static final String VIAVERS = "[ViaVersion] There is a newer plugin version available:";
    private static final String FAWE = "(FAWE) An update for FastAsyncWorldEdit is available.";

    private static ServerMessageMuter muter = new ServerMessageMuter();

    public static void init() {
        PacketListenerRegistry.register(muter);
    }

    private PacketEventResult mutePunishmentAttempts(String s) {
        Matcher speechMatcher = FAILEDSPEECH.matcher(s);
        Matcher joinMatcher = FAILEDJOIN.matcher(s);
        if (speechMatcher.find() || joinMatcher.find()) return PacketEventResult.CANCEL;
        else return null;
    }

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketEventResult.PASS;
        String string = msgText.getString();
        if (string.startsWith(VIAVERS) || string.startsWith(FAWE)) return PacketEventResult.CANCEL;
        if (ModUtilsConfig.config.hideJoinMessages && mutePunishmentAttempts(string) != null) return mutePunishmentAttempts(string);
        return switch (string) {
            case "[Server: Automatic saving is now enabled]",
                 "[Server: Automatic saving is now disabled]",
                 "[Server: Saved the game]" -> PacketEventResult.CANCEL;
            default -> PacketEventResult.PASS;
        };
    }
}
