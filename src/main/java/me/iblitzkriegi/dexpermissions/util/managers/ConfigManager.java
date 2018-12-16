package me.iblitzkriegi.dexpermissions.util.managers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

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
        plugin.saveDefaultConfig();

    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe("Unable to save the config!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }


}
