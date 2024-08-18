package updater;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class YamlUpdater {

    public void updateYamlFileWithRemote(String remoteUrl, File localFile) {
        try {
            FileConfiguration localConfig = YamlConfiguration.loadConfiguration(localFile);

            InputStream inputStream = new URL(remoteUrl).openStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            FileConfiguration remoteConfig = YamlConfiguration.loadConfiguration(reader);

            mergeConfigurations(localConfig, remoteConfig);

            localConfig.save(localFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mergeConfigurations(FileConfiguration localConfig, FileConfiguration remoteConfig) {
        for (String key : remoteConfig.getKeys(true)) {
            if (!localConfig.contains(key)) {
                localConfig.set(key, remoteConfig.get(key));
            }
        }
    }
}
