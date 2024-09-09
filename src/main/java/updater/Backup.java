package updater;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Backup {
    private final Plugin plugin;

    public Backup(Plugin plugin) {
        this.plugin = plugin;
    }

    public void createBackup() {
        File tchatDir = new File(plugin.getDataFolder().getParentFile(), "TChat");
        File backupDir = new File(tchatDir, "backups");

        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String backupName = "backup_" + timeStamp;
        File currentBackup = new File(backupDir, backupName);

        currentBackup.mkdirs();

        copyDirectory(tchatDir, currentBackup, backupDir);

        plugin.getLogger().info("Backup complete: " + currentBackup.getAbsolutePath());
    }

    private void copyDirectory(@NotNull File source, File target, File excludeDir) {
        if (source.isDirectory()) {
            if (source.equals(excludeDir)) {
                return;
            }

            if (!target.exists()) {
                target.mkdirs();
            }

            for (String file : Objects.requireNonNull(source.list())) {
                File srcFile = new File(source, file);
                File destFile = new File(target, file);

                copyDirectory(srcFile, destFile, excludeDir);
            }
        } else {
            try {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
