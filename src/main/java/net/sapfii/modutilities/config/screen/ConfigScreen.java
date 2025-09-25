package net.sapfii.modutilities.config.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.ModUtilsConfig;
import net.sapfii.sapscreens.SapScreens;
import net.sapfii.sapscreens.screens.widgets.*;

import java.util.Arrays;
import java.util.List;


public class ConfigScreen extends WidgetContainerScreen {

    public ConfigScreen(Screen previousScreen) {
        super(previousScreen);
        addWidgets(
                new WidgetListBox(
                        new TextDisplayWidget(Text.literal("ModUtilities Config").withColor(0x55ff55), 7, TextDisplayWidget.Alignment.CENTER),
                        new TextDisplayWidget(Text.literal("awesome sauce").withColor(0xffaad4), 7, TextDisplayWidget.Alignment.CENTER),
                        new WidgetListBox(
                                new TextDisplayWidget(Text.literal("Overlays"), 7, TextDisplayWidget.Alignment.CENTER),
                                new ButtonWidget(Text.literal("Use Vanish Overlay - " + ModUtilsConfig.config.useVanishDisplay), (button) -> {
                                    ModUtilsConfig.config.useVanishDisplay = !ModUtilsConfig.config.useVanishDisplay;
                                    button.withText(Text.literal("Use Vanish Overlay - " + ModUtilsConfig.config.useVanishDisplay));
                                }),
                                new ButtonWidget(Text.literal("Use Report Overlay - " + ModUtilsConfig.config.useReportDisplay), (button) -> {
                                    ModUtilsConfig.config.useReportDisplay = !ModUtilsConfig.config.useReportDisplay;
                                    button.withText(Text.literal("Use Report Overlay - " + ModUtilsConfig.config.useReportDisplay));
                                }),
                                new ButtonWidget(Text.literal("Report Overlay Style - " + ModUtilsConfig.config.reportType), (button) -> {
                                    List<ModUtilsConfig.REPORT_TYPE> constants = Arrays.asList(ModUtilsConfig.REPORT_TYPE.class.getEnumConstants());
                                    int index = constants.indexOf(ModUtilsConfig.config.reportType);
                                    index = ModUtilities.wrap(index + 1, constants.size());
                                    ModUtilsConfig.config.reportType = constants.get(index);
                                    button.withText(Text.literal("Report Overlay Style - " + ModUtilsConfig.config.reportType));
                                })
                        )
                                .withDimensions(0, 85)
                                .withPadding(5, 50, 5),
                        new TextDisplayWidget(Text.empty(), 7, TextDisplayWidget.Alignment.CENTER),
                        new WidgetListBox(
                                new TextDisplayWidget(Text.literal("Screens"), 7, TextDisplayWidget.Alignment.CENTER),
                                new ButtonWidget(Text.literal("Use Log Screen - " + ModUtilsConfig.config.useLogScreen), (button) -> {
                                    ModUtilsConfig.config.useLogScreen = !ModUtilsConfig.config.useLogScreen;
                                    button.withText(Text.literal("Use Log Screen - " + ModUtilsConfig.config.useLogScreen));
                                }),
                                new ButtonWidget(Text.literal("Log Screen Scroll Direction - " + ModUtilsConfig.config.logDirection), (button) -> {
                                    List<ModUtilsConfig.LOG_DIRECTION> constants = Arrays.asList(ModUtilsConfig.LOG_DIRECTION.class.getEnumConstants());
                                    int index = constants.indexOf(ModUtilsConfig.config.logDirection);
                                    index = ModUtilities.wrap(index + 1, constants.size());
                                    ModUtilsConfig.config.logDirection = constants.get(index);
                                    button.withText(Text.literal("Log Screen Scroll Direction - " + ModUtilsConfig.config.logDirection));
                                }),
                                new ButtonWidget(Text.literal("Use History Screen - " + ModUtilsConfig.config.useHistoryScreen), (button) -> {
                                    ModUtilsConfig.config.useHistoryScreen = !ModUtilsConfig.config.useHistoryScreen;
                                    button.withText(Text.literal("Use History Screen - " + ModUtilsConfig.config.useHistoryScreen));
                                })
                        )
                                .withDimensions(0, 85)
                                .withPadding(5, 50, 5),
                        new WidgetListBox(
                                new TextDisplayWidget(Text.literal("Misc"), 7, TextDisplayWidget.Alignment.CENTER),
                                new ButtonWidget(Text.literal("Mute Join Attempts - " + ModUtilsConfig.config.hideJoinMessages), (button) -> {
                                    ModUtilsConfig.config.hideJoinMessages = !ModUtilsConfig.config.hideJoinMessages;
                                    button.withText(Text.literal("Mute Join Attempts - " + ModUtilsConfig.config.hideJoinMessages));
                                })
                        )
                                .withDimensions(0, 85)
                                .withPadding(5, 50, 5)

                )
                        .withPosition(50, 50)
                        .centerOnParent(true, true)
                        .withPadding(10, 50, 7)
        );
    }

    @Override
    public void close() {
        ModUtilsConfig.saveConfig();
        super.close();
    }

    @Override
    public void removeWidget(Widget widget) {
        elements.remove(widget);
    }
}
