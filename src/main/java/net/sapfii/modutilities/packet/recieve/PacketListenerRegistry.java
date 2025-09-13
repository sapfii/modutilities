package net.sapfii.modutilities.packet.recieve;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;

import java.util.ArrayList;
import java.util.List;

public class PacketListenerRegistry {

    private static final List<ClientPacketListener> listeners = new ArrayList<>();

    public static void register(ClientPacketListener listener) {
        listeners.add(listener);
    }

    public static <T extends PacketListener> ClientPacketListener.PacketEventResult handle(Packet<T> packet) {
        if (packet instanceof BundleS2CPacket bundle) {
            bundle.getPackets().forEach(PacketListenerRegistry::handle);
            return ClientPacketListener.PacketEventResult.PASS;
        }
        for (ClientPacketListener listener : listeners) {
            if (listener.onPacket(packet) == ClientPacketListener.PacketEventResult.CANCEL) {
                return ClientPacketListener.PacketEventResult.CANCEL;
            }
        }
        return ClientPacketListener.PacketEventResult.PASS;
    }
}
