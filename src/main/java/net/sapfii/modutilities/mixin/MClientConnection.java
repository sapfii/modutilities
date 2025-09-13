package net.sapfii.modutilities.mixin;


import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class MClientConnection {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onPacketReceived(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (PacketListenerRegistry.handle(packet) == ClientPacketListener.PacketEventResult.CANCEL) ci.cancel();
    }
}
