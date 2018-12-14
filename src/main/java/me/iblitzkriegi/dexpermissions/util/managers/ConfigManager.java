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
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        file = new File(plugin.getDataFolder(), "config.yml");
        saveConfig();
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
