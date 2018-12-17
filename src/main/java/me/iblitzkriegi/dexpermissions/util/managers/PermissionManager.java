package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.DexPermissions;
import me.iblitzkriegi.dexpermissions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import sun.security.krb5.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PermissionManager {

    public static HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();

    public static void setupPermissions(Player player) {
        DexPermissions dexPermissions = DexPermissions.getInstance();
        FileConfiguration config = dexPermissions.getConfig();
        PermissionAttachment permissionAttachment = player.addAttachment(dexPermissions);
        String uuid = Util.getUniqueId(player);
        playerPermissions.put(uuid, permissionAttachment);
        for (String user : config.getConfigurationSection("Users").getKeys(false)) {
            if (uuid.equalsIgnoreCase(user)) {
                String group = config.getConfigurationSection("Users." + uuid).getString("group");
                for (String permission : config.getConfigurationSection("Groups." + group).getStringList("permissions")) {
                    permissionAttachment.setPermission(permission, true);
                }
                for (String permission : config.getStringList("Users." + uuid + ".permissions")) {
                    permissionAttachment.setPermission(permission, true);
                }
            }
        }
    }

    public static void addPermission(Player player, String permission) {
        String uuid = Util.getUniqueId(player);
        PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
        FileConfiguration config = ConfigManager.getInstance().config;
        permissionAttachment.setPermission(permission, true);
        ConfigurationSection section = config.getConfigurationSection("Users." + uuid);
        List<String> permissions = section.getStringList("permissions");
        if (permission.isEmpty()) {
            List<String> setPermissions = new ArrayList<>();
            setPermissions.add(permission);
            section.set("permissions", setPermissions);
        } else {
            if (permissions.contains(permission)) {
                return;
            }
            permissions.add(permission);
            section.set("permissions", permissions);
        }
    }

    public static void removePermission(Player player, String permission) {
        String uuid = Util.getUniqueId(player);
        PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
        FileConfiguration config = ConfigManager.getInstance().config;
        permissionAttachment.setPermission(permission, false);
        ConfigurationSection section = config.getConfigurationSection("Users." + uuid);
        List<String> permissions = section.getStringList("permissions");
        if (!permissions.isEmpty()) {
            if (permission.contains(permission)) {
                permissions.remove(permission);
                section.set("permissions", permissions);
            }
        }
    }

    public static void addPermission(String group, String permission) {
        FileConfiguration config = ConfigManager.getInstance().config;
        if (config.getConfigurationSection("Groups." + group) == null) {
            return;
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String uuid = Util.getUniqueId(player);
            if (config.getString("Users." + uuid + ".group").equalsIgnoreCase(group)) {
                PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
                permissionAttachment.setPermission(permission, true);
            }
        }
        ConfigurationSection configurationSection = config.getConfigurationSection("Groups." + group);
        List<String> permissions = configurationSection.getStringList("permissions");
        if (permissions.isEmpty()) {
            List<String> setPermissions = new ArrayList<>();
            setPermissions.add(permission);
            configurationSection.set("permissions", setPermissions);
        } else {
            if (permissions.contains(permission)) {
                return;
            }
            permissions.add(permission);
            configurationSection.set("permissions", permissions);
        }
    }

    public static void removePermission(String group, String permission) {
        FileConfiguration config = ConfigManager.getInstance().config;
        ConfigurationSection configurationSection = config.getConfigurationSection("Groups." + group);
        if (configurationSection == null) return;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String uuid = Util.getUniqueId(player);
            ConfigurationSection playerSection = config.getConfigurationSection("Users." + uuid);
            if (playerSection.getString("group").equalsIgnoreCase(group)) {
                PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
                List<String> playerPermissions = playerSection.getStringList("permissions");
                if (!playerPermissions.isEmpty()) {
                    if (!playerPermissions.contains(permission)) {
                        permissionAttachment.setPermission(permission, false);
                    }
                } else {
                    permissionAttachment.setPermission(permission, false);
                }
            }
        }
        List<String> permissions = configurationSection.getStringList("permissions");
        if (permissions.isEmpty() || !permissions.contains(permission)) return;
        permissions.remove(permission);
        configurationSection.set("permissions", permissions);

    }

    public static void setGroup(Player player, String group) {
        FileConfiguration config = ConfigManager.getInstance().config;
        String configGroup = config.getString("Groups." + group);
        if (configGroup == null) return;

        String uuid = Util.getUniqueId(player);
        PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
        ConfigurationSection section = config.getConfigurationSection("Users." + uuid);
        String currentGroup = section.getString("group");
        for (String permission : config.getConfigurationSection("Groups." + currentGroup).getStringList("permissions")) {
            permissionAttachment.unsetPermission(permission);
        }
        for (String permission : config.getConfigurationSection("Groups." + group).getStringList("permissions")) {
            permissionAttachment.setPermission(permission, true);
        }
        section.set("group", group);
    }


}
