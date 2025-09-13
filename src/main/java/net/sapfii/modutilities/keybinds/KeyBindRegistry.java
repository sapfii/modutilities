package net.sapfii.modutilities.keybinds;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class KeyBindRegistry {
    public static List<KeyBindListener> listeners = new ArrayList<>();

    public static void registerKeyBind(KeyBinding keyBind) {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBind.wasPressed()) {
                if (listeners.isEmpty()) return;
                for (KeyBindListener listener : listeners) {
                    listener.keyPressed(keyBind);
                }
            }
        });
    }

    public static void registerListener(KeyBindListener listener) {
        listeners.add(listener);
    }
}
