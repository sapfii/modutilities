package net.sapfii.modutilities.packet.send;

import java.util.ArrayList;
import java.util.List;



public class CommandListenerRegistry {

    private static final List<ClientCommandListener> listeners = new ArrayList<>();

    public static void register(ClientCommandListener listener) {
        listeners.add(listener);
    }

    public static ClientCommandListener.CommandEventResult onCommand(String message) {
        for (ClientCommandListener listener : listeners) {
            if (listener.onCommand(message) == ClientCommandListener.CommandEventResult.CANCEL) {
                return ClientCommandListener.CommandEventResult.CANCEL;
            }
        }
        return ClientCommandListener.CommandEventResult.PASS;
    }
}
