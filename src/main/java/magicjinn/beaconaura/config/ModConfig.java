package magicjinn.beaconaura.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Simple mod config class, who gives a shit
public class ModConfig {
    // Gameplay configuration
    public static int extraSecondsPerLevel = 4;
    public static int maxMinutesPerLevel = 15;
    public static int rangeBase = 32;
    public static int rangePerLevel = 32;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("beacon-aura.json")
            .toFile();

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save(); // Create the config file if it doesn't exist
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                extraSecondsPerLevel = data.extraSecondsPerLevel;
                maxMinutesPerLevel = data.maxMinutesPerLevel;
                rangeBase = data.rangeBase;
                rangePerLevel = data.rangePerLevel;
            }
        } catch (IOException e) {
            save();
            System.err.println("Failed to load Beacon Aura config: " + e.getMessage() + ". Making a new one.");
        }
    }

    public static void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                ConfigData data = new ConfigData();
                data.extraSecondsPerLevel = extraSecondsPerLevel;
                data.maxMinutesPerLevel = maxMinutesPerLevel;
                data.rangeBase = rangeBase;
                data.rangePerLevel = rangePerLevel;
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save Beacon Aura config: " + e.getMessage());
        }
    }

    private static class ConfigData {
        int extraSecondsPerLevel = 4;
        int maxMinutesPerLevel = 15;
        int rangeBase = 10;
        int rangePerLevel = 10;
    }
}
