package net.sapfii.modutilities.packet.send;

public interface ClientCommandListener {
    enum CommandEventResult { PASS, CANCEL }
    CommandEventResult onCommand(String command);
}
