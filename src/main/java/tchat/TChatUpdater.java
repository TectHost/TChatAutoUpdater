package tchat;

import managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import updater.YamlUpdater;

import java.io.File;

public class TChatUpdater extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "TChat" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Starting TChatUpdater...");

        registerConfigFiles();

        if (configManager.isAllDisabled()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "All updates are disabled in the configuration.");
            return;
        }

        String baseUrl = "https://raw.githubusercontent.com/TectHost/TChat/main/src/main/resources/";

        String[] files = {
                "autobroadcast.yml",
                "banned_commands.yml",
                "banned_words.yml",
                "channels.yml",
                "chatbot.yml",
                "chatgames.yml",
                "command_programmer.yml",
                "command_timer.yml",
                "commands.yml",
                "config.yml",
                "death.yml",
                "groups.yml",
                "joins-yml",
                "levels.yml",
                "messages.yml",
                "placeholders.yml",
                "replacer.yml",
                "saves.yml",
                "worlds.yml"
        };

        String[] folders = {
                "hooks/discord.yml",
                "menus/chatcolor.yml"
        };

        File tchatDir = new File(getDataFolder().getParentFile(), "TChat");
        if (!tchatDir.exists()) {
            tchatDir.mkdirs();
        }

        for (String fileName : files) {
            String file = fileName.substring(0, fileName.length() - 4);
            if (configManager.shouldUpdateFile(file)) {
                File localFile = new File(tchatDir, fileName);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Updated file: " + fileName);
                synchronizeFile(baseUrl + fileName, localFile);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Skipping update for " + fileName);
            }
        }

        for (String folderFile : folders) {
            String[] parts = folderFile.split("/", 2);
            String folderName = parts[0];
            String subFileName = parts[1];

            subFileName = subFileName.substring(0, subFileName.length() - 4);

            if (configManager.shouldUpdateFolderFile(folderName, subFileName)) {
                File file = new File(tchatDir, folderFile);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Updated file: " + folderFile);
                synchronizeFile(baseUrl + folderFile, file);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Skipping update for " + folderFile);
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "TChat" + ChatColor.GRAY + "] " + ChatColor.GREEN + "TChatUpdater started.");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "TChat" + ChatColor.GRAY + "] " + ChatColor.RED + "TChatUpdater stopped");
    }

    private void synchronizeFile(String remoteUrl, File localFile) {
        YamlUpdater yamlUpdater = new YamlUpdater();

        if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
        }

        yamlUpdater.updateYamlFileWithRemote(remoteUrl, localFile);
    }

    private void registerConfigFiles() {
        configManager = new ConfigManager(this);
    }
}
