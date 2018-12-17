package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

import static me.iblitzkriegi.dexpermissions.util.managers.PermissionManager.playerPermissions;

public class ConfigManager {

    static ConfigManager instance = new ConfigManager();
    Plugin plugin;
    FileConfiguration config;
    File file;

    public static ConfigManager getInstance() {
        return instance;
    }

    public void setup(Plugin plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        file = new File(plugin.getDataFolder(), "config.yml");
        plugin.saveDefaultConfig();

    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("Unable to save the config!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        for (Player player : Bukkit.getOnlinePlayers()) {
            PermissionAttachment permissionAttachment = playerPermissions.get(Util.getUniqueId(player));
            permissionAttachment.remove();
            PermissionManager.setupPermissions(player);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
