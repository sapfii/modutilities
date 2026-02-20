package net.sapfii.modutilities.mixin;


import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ChatScreen;
import net.sapfii.modutilities.features.Features;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MChatScreen {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void mouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        Features.onClick((int) click.x(), (int) click.y());
    }

}
