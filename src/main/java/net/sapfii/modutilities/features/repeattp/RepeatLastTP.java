package net.sapfii.modutilities.features.repeattp;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.features.vanish.VanishTracker;
import net.sapfii.modutilities.keybinds.KeyBindListener;
import net.sapfii.modutilities.keybinds.KeyBindRegistry;
import net.sapfii.modutilities.keybinds.ModUtilsKeyBinds;
import net.sapfii.modutilities.packet.send.ClientCommandListener;
import net.sapfii.modutilities.packet.send.CommandListenerRegistry;

public class RepeatLastTP implements ClientCommandListener, KeyBindListener {
    private static final RepeatLastTP handler = new RepeatLastTP();
    private static String lastTpCmd = "";

    @Override
    public CommandEventResult onCommand(String command) {
        if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.NONE) return CommandEventResult.PASS;
        if (command.startsWith("tp ")) lastTpCmd = command;
        return CommandEventResult.PASS;
    }

    @Override
    public void keyPressed(KeyBinding keyBind) {
        if (keyBind.equals(ModUtilsKeyBinds.REPEAT_TP)) {
            if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.NONE) {
                ModUtilities.sendMessage(Text.literal("Error: ").formatted(Formatting.RED).append(Text.literal("You need to be in vanish to do this!").formatted(Formatting.GRAY)), false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
                return;
            }
            if (!lastTpCmd.isEmpty()) {
                ModUtilities.sendCommand(lastTpCmd);
                ModUtilities.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1f);
            } else {
                ModUtilities.sendMessage(Text.literal("Error: ").formatted(Formatting.RED).append(Text.literal("You have not done a TP command to repeat!").formatted(Formatting.GRAY)), false);
                ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1f);
            }
        }
    }

    public static void init() {
        CommandListenerRegistry.register(handler);
        KeyBindRegistry.registerListener(handler);
    }
}
