package me.iblitzkriegi.dexpermissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DexPermissions extends JavaPlugin {

    private DexPermissions() {}

    static DexPermissions instance = new DexPermissions();
    public HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();

    public static DexPermissions getInstance() {
        return instance;
    }

    Plugin plugin;
    FileConfiguration config;
    File file;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults(true);
        file = new File(getDataFolder(), "config.yml");
        saveConfig();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}
