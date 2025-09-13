package net.sapfii.modutilities.features.playertracker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.report.ReportHandler;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerTracker implements ClientPacketListener {
    private static final Pattern TP_MSG = Pattern.compile("Teleported (.+) to (.+)");

    public static PlayerTracker handler = new PlayerTracker();
    private static HashMap<String, Entity> trackedEntities = new HashMap<>();
    private static boolean awaitingCommandResponse = false;
    private static boolean awaitingTeleportPacket = false;
    private static Entity closestEntity;

    public static void addPlayer(String playerName) {
        ModUtilities.sendCommand("tp " + playerName);
        awaitingTeleportPacket = true;
    }

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay)) {
            if (!awaitingCommandResponse) return PacketEventResult.PASS;
            String string = msgText.getString();
            Matcher matcher = TP_MSG.matcher(string);
            if (matcher.find()) {
                trackedEntities.put(matcher.group(2), closestEntity);
                ModUtilities.sendMessage(Text.literal("Tracking " + matcher.group(2)), false);
                ModUtilities.sendMessage(closestEntity.getName(), false);
                if (!matcher.group(2).matches(closestEntity.getName().getString())) {
                    ModUtilities.sendMessage(Text.literal("WARNING: Entity name does not match player name! This means the player is either disguised or the matcher got the wrong player. Please re-run the command again to confirm.").formatted(Formatting.RED), false);
                }
                awaitingCommandResponse = false;
            }
            if (string.matches("No entity was found")) {
                awaitingCommandResponse = false;
                awaitingTeleportPacket = false;
            }
            return PacketEventResult.PASS;
        }
        if (packet instanceof PlayerPositionLookS2CPacket tpPacket) {
            if (!awaitingTeleportPacket) return PacketEventResult.PASS;
            PlayerPosition change = tpPacket.change();
            float lowestDistance = 9999f;
            for (Entity entity : ModUtilities.MC.world.getEntities()) {
                if (entity == ModUtilities.MC.player) continue;
                float distance = Vector3f.distance(
                        (float) entity.getX(),
                        (float) entity.getY(),
                        (float) entity.getZ(),
                        (float) (change.position().getX() + entity.getVelocity().x),
                        (float) (change.position().getY() + entity.getVelocity().y),
                        (float) (change.position().getZ() + entity.getVelocity().z));
                System.out.println(entity.getVelocity().x);
                if (distance < lowestDistance) {
                    closestEntity = entity;
                    lowestDistance = distance;
                }
            }
            awaitingTeleportPacket = false;
            awaitingCommandResponse = true;
            return PacketEventResult.PASS;
        }
        return PacketEventResult.PASS;
    }

    public static void init() {
        PacketListenerRegistry.register(handler);
    }
}
