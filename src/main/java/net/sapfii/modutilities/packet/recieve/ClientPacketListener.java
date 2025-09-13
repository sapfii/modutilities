package net.sapfii.modutilities.packet.recieve;

import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

public interface ClientPacketListener {
    enum PacketEventResult { PASS, CANCEL }
    <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet);
}
