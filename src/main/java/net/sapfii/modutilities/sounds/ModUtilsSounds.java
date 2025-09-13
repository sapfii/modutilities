package net.sapfii.modutilities.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.sapfii.modutilities.ModUtilities;

public class ModUtilsSounds {
    private ModUtilsSounds() {}

    public static final SoundEvent REPORT_DISMISS = registerSound("report_dismiss");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.of(ModUtilities.MOD_ID, id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void init() {
        ModUtilities.LOGGER.info("initializing sounds");
    }
}
