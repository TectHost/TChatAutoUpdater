package managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tchat.TChatUpdater;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {

    private final FileConfiguration config;

    public ConfigManager(TChatUpdater plugin) {
        File configFile = new File(plugin.getDataFolder().getParentFile(), "TChat/addons/updater.yml");

        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                try (InputStream in = plugin.getResource("updater.yml")) {
                    assert in != null;
                    Files.copy(in, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("Error creating updater.yml: " + e.getMessage());
            }
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public boolean isAllDisabled() {
        return config.getBoolean("all-disabled", false);
    }

    public boolean shouldUpdateFile(String fileName) {
        return config.getBoolean("files." + fileName, false);
    }

    public boolean shouldUpdateFolderFile(String folderName, String fileName) {
        return config.getBoolean("folders." + folderName + "." + fileName, false);
    }
}
