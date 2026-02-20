package net.sapfii.modutilities.config;

import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.sapfii.modutilities.ModUtilities;
import net.velli.scelli.screen.WidgetContainerScreen;
import net.velli.scelli.widget.widgets.Alignment;
import net.velli.scelli.widget.widgets.ButtonWidget;
import net.velli.scelli.widget.widgets.TextDisplayWidget;
import net.velli.scelli.widget.widgets.Widgets;
import net.velli.scelli.widget.widgets.containers.VerticalListWidget;


public class ConfigScreen extends WidgetContainerScreen {

    public ConfigScreen() {
        super(null);
        ModUtilsConfig config = ModUtilsConfig.config;
        addWidgets(
                Widgets.create(VerticalListWidget::new)
                        .addWidgets(
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 11).setLines(Text.translatable("text.modutilities.config_title").withColor(0x55FF55)).withTextAlignment(Alignment.CENTER),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.empty()),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.translatable("text.modutilities.config_category.overlays")).withTextAlignment(Alignment.CENTER),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.vanish_overlay." + (config.useVanishDisplay.get() ? "on" : "off")))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.useVanishDisplay.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.vanish_overlay." + (config.useVanishDisplay.get() ? "on" : "off")));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        }),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.report_overlay." + (config.useReportDisplay.get() ? "on" : "off")))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.useReportDisplay.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.report_overlay." + (config.useReportDisplay.get() ? "on" : "off")));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        }),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.report_overlay_style." + config.reportType.get().id()))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.reportType.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.report_overlay_style." + config.reportType.get().id()));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        }),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 11).setLines(Text.empty()),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.translatable("text.modutilities.config_category.miscellaneous")).withTextAlignment(Alignment.CENTER),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.hide_join_messages." + (config.hideJoinMessages.get() ? "on" : "off")))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.hideJoinMessages.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.hide_join_messages." + (config.hideJoinMessages.get() ? "on" : "off")));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        })

                        )
                        .newColumn()
                        .addWidgets(
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 11).setLines(Text.translatable("text.modutilities.config_subtitle").withColor(0xFFAAD4)).withTextAlignment(Alignment.CENTER),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.empty()),
                                Widgets.create(TextDisplayWidget::new, 0, 0, 150, 16).setLines(Text.translatable("text.modutilities.config_category.screens")).withTextAlignment(Alignment.CENTER),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.log_screen." + (config.useLogScreen.get() ? "on" : "off")))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.useLogScreen.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.log_screen." + (config.useLogScreen.get() ? "on" : "off")));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        }),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.log_direction." + config.logDirection.get().id()))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.logDirection.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.log_direction." + config.logDirection.get().id()));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        }),
                                Widgets.create(ButtonWidget::new, 0, 0, 150, 16)
                                        .withText(Text.translatable("text.modutilities.history_screen." + (config.useHistoryScreen.get() ? "on" : "off")))
                                        .withClickEvent(new ButtonWidget.ClickEvent() {
                                            @Override
                                            public void onClick(ButtonWidget buttonWidget, int i, int i1) {
                                                if (!buttonWidget.isHovered()) return;
                                                config.useHistoryScreen.cycle();
                                                buttonWidget.withText(Text.translatable("text.modutilities.history_screen." + (config.useHistoryScreen.get() ? "on" : "off")));
                                                ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
                                            }
                                            @Override
                                            public void onRelease(ButtonWidget buttonWidget, int i, int i1) {}
                                        })
                        )
                        .withAlignment(Alignment.CENTER)
                        .withPosition(0, -500, true)
                        .withPosition(0, 0, false)
                        .withDimensions(350, 200, true)
                        .withPadding(5, 20, 2, 4)
        );
//        addWidgets(
//                ListWidget.create()
//                        .addWidgets(
//                                TextWidget.create().withText(Text.translatable("text.modutilities.config_title").withColor(0x55FF55)).withAlignment(TextWidget.TextAlignment.CENTER),
//                                TextWidget.create().withText(Text.translatable("text.modutilities.config_subtitle").withColor(0xFFAAD4)).withAlignment(TextWidget.TextAlignment.CENTER),
//                                FolderWidget.create(false).addWidgets(
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.vanish_overlay." + (config.useVanishDisplay.get() ? "on" : "off")))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.useVanishDisplay.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.vanish_overlay." + (config.useVanishDisplay.get() ? "on" : "off")));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                }),
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.report_overlay." + (config.useReportDisplay.get() ? "on" : "off")))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.useReportDisplay.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.report_overlay." + (config.useReportDisplay.get() ? "on" : "off")));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                }),
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.report_overlay_style." + config.reportType.get().id()))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.reportType.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.report_overlay_style." + config.reportType.get().id()));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                })
//                                ).withTitle(Text.translatable("text.modutilities.config_category.overlays")),
//                                FolderWidget.create(false).addWidgets(
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.log_screen." + (config.useLogScreen.get() ? "on" : "off")))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.useLogScreen.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.log_screen." + (config.useLogScreen.get() ? "on" : "off")));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                }),
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.log_direction." + config.logDirection.get().id()))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.logDirection.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.log_direction." + config.logDirection.get().id()));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                }),
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.history_screen." + (config.useHistoryScreen.get() ? "on" : "off")))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.useHistoryScreen.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.history_screen." + (config.useHistoryScreen.get() ? "on" : "off")));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                })
//                                ).withTitle(Text.translatable("text.modutilities.config_category.screens")),
//                                FolderWidget.create(false).addWidgets(
//                                        ButtonWidget.create().withText(Text.translatable("text.modutilities.hide_join_messages." + (config.hideJoinMessages.get() ? "on" : "off")))
//                                                .withClickEvent((button, mouseX, mouseY, active) -> {
//                                                    if (!active || !button.hovered()) return;
//                                                    config.hideJoinMessages.cycle();
//                                                    button.withText(Text.translatable("text.modutilities.hide_join_messages." + (config.hideJoinMessages.get() ? "on" : "off")));
//                                                    ModUtilities.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1f);
//                                                })
//                                ).withTitle(Text.translatable("text.modutilities.config_category.miscellaneous"))
//                        )
//                        .withAlignment(WidgetPos.Alignment.CENTER)
//                        .withPosition(0, -500, true)
//                        .withPosition(0, 0, false)
//                        .withDimensions(300, 200, true)
//                        .withPadding(5, 10, 4)
//        );
    }

    @Override
    public void close() {
        ModUtilsConfig.saveConfig();
        super.close();
    }
}
