package net.sapfii.modutilities.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.screen.ConfigScreen;
import net.sapfii.modutilities.features.playertracker.PlayerTracker;
import net.sapfii.modutilities.features.report.screen.ReportsScreen;
import net.sapfii.modutilities.features.vanish.VanishTracker;
import net.sapfii.modutilities.ranktracker.RankTracker;
import net.sapfii.sapscreens.ScreenHandler;

public class ModUtilsCommands {
    public static void init() {
        registerSimpleCommand("reports", context -> {
            ScreenHandler.openScreen(new ReportsScreen(null));
            return 1;
        });
        registerSimpleCommand("modutils", context -> {
            ScreenHandler.openScreen(new ConfigScreen(null));
            return 1;
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("track")
                .then(ClientCommandManager.argument("player", StringArgumentType.word())
                        .executes(ModUtilsCommands::trackCommand))
            );
        });
    }

    private static int trackCommand(CommandContext<FabricClientCommandSource> context) {
        if (RankTracker.playerRank == RankTracker.Rank.NONE) {
            ModUtilities.sendMessage(Text.literal("you arent a mod u cant use this stupid"), false);
            return 1;
        }
        if (VanishTracker.tracker.vanishMode == VanishTracker.VanishMode.NONE) {
            ModUtilities.sendMessage(Text.literal("hey you can't use this outside of vanish silly"), false);
            return 1;
        }
        String playerName = StringArgumentType.getString(context, "player");
        if (playerName.matches(ModUtilities.MC.player.getName().getString())) {
            ModUtilities.sendMessage(Text.literal("goofy goober you can't track yourself"), false);
            return 1;
        }
        PlayerTracker.addPlayer(playerName);
        return 1;
    }

    private static void registerSimpleCommand(String id, Command<FabricClientCommandSource> command) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(id).executes(command));
        });
    }
}
