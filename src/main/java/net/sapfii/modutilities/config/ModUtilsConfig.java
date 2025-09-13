package net.sapfii.modutilities.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModUtilsConfig {
    public boolean useVanishDisplay = true;
    public boolean useReportDisplay = true;
    public boolean useHistoryScreen = true;
    public boolean useLogScreen = true;
    public enum LOG_DIRECTION {UP, DOWN}
    public LOG_DIRECTION logDirection = LOG_DIRECTION.UP;
    public enum REPORT_TYPE {CLASSIC, REVAMPED, REIMAGINED}
    public REPORT_TYPE reportType = REPORT_TYPE.REVAMPED;
    public boolean hideJoinMessages = true;

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("modutils.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ModUtilsConfig config = new ModUtilsConfig();

    public static void loadConfig(){
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                config = GSON.fromJson(reader, ModUtilsConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
