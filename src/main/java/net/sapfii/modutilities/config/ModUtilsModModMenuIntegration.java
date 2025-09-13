package net.sapfii.modutilities.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.sapfii.modutilities.ModUtilities;
import net.sapfii.modutilities.config.screen.ConfigScreen;

public class ModUtilsModModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> new ConfigScreen(ModUtilities.MC.currentScreen);
    }
}
