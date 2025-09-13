package net.sapfii.modutilities.features.vanish;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.packet.recieve.ClientPacketListener;
import net.sapfii.modutilities.packet.recieve.PacketListenerRegistry;
import net.sapfii.modutilities.packet.send.ClientCommandListener;
import net.sapfii.modutilities.packet.send.CommandListenerRegistry;
import net.sapfii.modutilities.ranktracker.RankTracker;

public class VanishTracker implements ClientPacketListener, ClientCommandListener {
    public enum VanishMode { NONE, MOD, ADMIN }

    private static final Identifier VANISH_DISPLAY_LAYER = Identifier.of("modutils", "vanish-display-layer");

    private static final Text ENTER_ADMIN_VANISH = Text.literal("[ADMIN]").withColor(0xFF0000).append(Text.literal(" You are now vanished!").withColor(0xFFFFFF));
    private static final Text EXIT_ADMIN_VANISH = Text.literal("[ADMIN]").withColor(0xFF0000).append(Text.literal(" You are no longer vanished.").withColor(0xFFFFFF));
    private static final Text ENTER_MOD_VANISH = Text.literal("[MOD]").withColor(0x00AA00).append(Text.literal(" You are now vanished!").withColor(0xFFFFFF));
    private static final Text EXIT_MOD_VANISH = Text.literal("[MOD]").withColor(0x00AA00).append(Text.literal(" You are no longer vanished.").withColor(0xFFFFFF));
    private static final Text OVERLAPPING_VANISH = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You cannot be in both Admin Vanish and Mod Vanish.").withColor(0xAAAAAA));
    private static final Text ADMIN_VANISH_FAILED2 = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You are already in Admin Vanish.").withColor(0xAAAAAA));
    private static final Text ADMIN_VANISH_FAILED3 = Text.literal("Error:").withColor(0xFF5555).append(Text.literal(" You are already out of Admin Vanish.").withColor(0xAAAAAA));


    public VanishMode vanishMode;
    private boolean exeModVCmd;
    private boolean exeAdminVCmd;
    private boolean adminExpectingWelcomeMsg;
    private boolean modExpectingWelcomeMsg;

    public static VanishTracker tracker = new VanishTracker();

    public static void init() {
        HudRenderCallback.EVENT.register(VANISH_DISPLAY_LAYER, VanishDisplay::render);
        PacketListenerRegistry.register(tracker);
        CommandListenerRegistry.register(tracker);
    }

    @Override
    public <T extends PacketListener> PacketEventResult onPacket(Packet<T> packet) {
        if (!(packet instanceof GameMessageS2CPacket(Text msgText, boolean overlay))) return PacketEventResult.PASS;
        String string = msgText.getString();
        if (string.matches("\\[ADMIN] You are currently vanished!")) {
            vanishMode = VanishMode.ADMIN;
            adminExpectingWelcomeMsg = true;
            return PacketEventResult.CANCEL;
        }
        if (string.matches("◆ Welcome back to DiamondFire! ◆")) {
            if (!modExpectingWelcomeMsg && !adminExpectingWelcomeMsg) {
                vanishMode = VanishMode.NONE;
            }
            modExpectingWelcomeMsg = false;
            adminExpectingWelcomeMsg = false;
        }
        return switch (string) {
            case "» Vanish enabled. You will not be visible to other players." -> {
                if (exeModVCmd) {
                    vanishMode = VanishMode.MOD;
                    exeModVCmd = false;
                    ModUtilities.sendMessage(ENTER_MOD_VANISH, false);
                } else if (!exeAdminVCmd) {
                    modExpectingWelcomeMsg = true;
                    vanishMode = VanishMode.MOD;
                    ModUtilities.sendMessage(ENTER_MOD_VANISH, false);
                } else {
                    exeAdminVCmd = false;
                    vanishMode = VanishMode.ADMIN;
                    ModUtilities.sendMessage(ENTER_ADMIN_VANISH, false);
                    yield PacketEventResult.CANCEL;
                }
                yield PacketEventResult.CANCEL;
            }
            case "» Vanish disabled. You will now be visible to other players." -> {
                if (exeModVCmd) {
                    exeModVCmd = false;
                    ModUtilities.sendMessage(EXIT_MOD_VANISH, false);
                } else if (!exeAdminVCmd) {
                    ModUtilities.sendMessage(EXIT_MOD_VANISH, false);
                } else {
                    exeAdminVCmd = false;
                    ModUtilities.sendMessage(EXIT_ADMIN_VANISH, false);
                }
                vanishMode = VanishMode.NONE;
                yield PacketEventResult.CANCEL;
            }
            default -> {
                exeAdminVCmd = false;
                exeModVCmd = false;
                yield PacketEventResult.PASS;
            }
        };
    }

    @Override
    public CommandEventResult onCommand(String command) {
        return switch (command) {
            case "mod v",
                 "mod vanish" -> {
                if (RankTracker.playerRank == RankTracker.Rank.NONE) yield CommandEventResult.PASS;
                if (vanishMode == VanishMode.ADMIN) {
                    ModUtilities.sendMessage(OVERLAPPING_VANISH, false);
                    ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0F);
                    yield CommandEventResult.CANCEL;
                }
                exeModVCmd = true;
                yield CommandEventResult.PASS;
            }
            case "s",
                 "spawn" -> {
                if (vanishMode == VanishMode.MOD) {
                    vanishMode = VanishMode.NONE;
                    ModUtilities.sendMessage(EXIT_MOD_VANISH, false);
                }
                yield CommandEventResult.PASS;
            }
            case "adminv off" -> {
                if (RankTracker.playerRank != RankTracker.Rank.ADMIN) yield CommandEventResult.PASS;
                if (vanishMode != VanishMode.ADMIN) {
                    ModUtilities.sendMessage(ADMIN_VANISH_FAILED3, false);
                    ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0F);
                    yield CommandEventResult.CANCEL;
                }
                exeAdminVCmd = true;
                yield CommandEventResult.PASS;
            }
            case "adminv on" -> {
                if (RankTracker.playerRank != RankTracker.Rank.ADMIN) yield CommandEventResult.PASS;
                if (vanishMode == VanishMode.MOD) {
                    ModUtilities.sendMessage(OVERLAPPING_VANISH, false);
                    ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0F);
                    yield CommandEventResult.CANCEL;
                }
                if (vanishMode == VanishMode.ADMIN) {
                    ModUtilities.sendMessage(ADMIN_VANISH_FAILED2, false);
                    ModUtilities.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0F);
                    yield CommandEventResult.CANCEL;
                }
                exeAdminVCmd = true;
                yield CommandEventResult.PASS;
            }
            default -> CommandEventResult.PASS;
        };
    }
}
