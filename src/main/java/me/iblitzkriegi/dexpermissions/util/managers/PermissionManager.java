package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.DexPermissions;
import me.iblitzkriegi.dexpermissions.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.iblitzkriegi.dexpermissions.util.managers.ConfigManager.defaultGroup;

public class PermissionManager {

    public static HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();
    public static HashMap<String, String> groupPrefixes = new HashMap<>();
    public static HashMap<String, String> groupSuffixes = new HashMap<>();

    public static void setupPermissions(Player player) {
        DexPermissions dexPermissions = DexPermissions.getInstance();
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        PermissionAttachment permissionAttachment = player.addAttachment(dexPermissions);
        String uuid = Util.getUniqueId(player);
        playerPermissions.put(uuid, permissionAttachment);
        if (config.getConfigurationSection("Users." + uuid) == null) {
            ConfigurationSection userSection = config.createSection("Users." + uuid);
            userSection.set("permissions", new ArrayList<>());
            if (ConfigManager.getDefaultGroup()) {
                for (String permission : config.getConfigurationSection("Groups." + defaultGroup).getStringList("permissions")) {
                    permissionAttachment.setPermission(permission, true);
                }
                userSection.set("group", defaultGroup);
            }
            ConfigManager.getInstance().saveConfig();
            return;
        }

        for (String permission : config.getConfigurationSection("Users." + uuid).getStringList("permissions")) {
            permissionAttachment.setPermission(permission, true);
        }

        String group = config.getConfigurationSection("Users." + uuid).getString("group");
        if (group == null) return;
        if (config.getConfigurationSection("Groups") == null) return;
        for (String permission : config.getConfigurationSection("Groups." + group).getStringList("permissions")) {
            permissionAttachment.setPermission(permission, true);
        }

    }

    public static void addPermission(Player player, String permission) {
        String uuid = Util.getUniqueId(player);
        PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
        FileConfiguration config = ConfigManager.getInstance().getConfig();
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
        ConfigManager.getInstance().saveConfig();
    }

    public static void removePermission(Player player, String permission) {
        String uuid = Util.getUniqueId(player);
        PermissionAttachment permissionAttachment = playerPermissions.get(uuid);
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        permissionAttachment.setPermission(permission, false);
        ConfigurationSection section = config.getConfigurationSection("Users." + uuid);
        List<String> permissions = section.getStringList("permissions");
        if (!permissions.isEmpty()) {
            if (permission.contains(permission)) {
                permissions.remove(permission);
                section.set("permissions", permissions);
            }
        }
        ConfigManager.getInstance().saveConfig();
    }

    public static void addPermission(String group, String permission) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
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
        ConfigManager.getInstance().saveConfig();
    }

    public static void removePermission(String group, String permission) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
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
        ConfigManager.getInstance().saveConfig();

    }

    public static void createGroup(String name) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        if (config.getConfigurationSection("Groups." + name) != null) return;
        config.createSection("Groups." + name).set("permissions", new ArrayList<>());
        ConfigManager.getInstance().saveConfig();
    }

    public static void deleteGroup(String name) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        if (config.getConfigurationSection("Groups." + name) == null) return;
        config.set("Groups." + name, null);
        groupSuffixes.remove(name);
        groupPrefixes.remove(name);
        ConfigManager.getInstance().saveConfig();
    }

    public static void setGroup(Player player, String group) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
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
        ConfigManager.getInstance().saveConfig();
    }

    public static void setGroupPrefix(String group, String prefix) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        ConfigurationSection groupSection = config.getConfigurationSection("Groups." + group);
        if (groupSection == null) return;
        groupSection.set("prefix", prefix);
        groupPrefixes.put(group, prefix);
        ConfigManager.getInstance().saveConfig();
    }

    public static String getGroupPrefix(String group) {
        return groupPrefixes.get(group);
    }

    public static void setGroupSuffix(String group, String suffix) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        ConfigurationSection groupSection = config.getConfigurationSection("Groups." + group);
        if (groupSection == null) return;
        groupSection.set("suffix", suffix);
        groupSuffixes.put(group, suffix);
        ConfigManager.getInstance().saveConfig();
    }

    public static String getGuildSuffix(String group) {
        return groupSuffixes.get(group);
    }

    public static String getGroup(Player player) {
        FileConfiguration config = ConfigManager.getInstance().getConfig();
        ConfigurationSection configurationSection = config.getConfigurationSection("Users." + Util.getUniqueId(player));
        String group = configurationSection.getString("group");
        return group == null ? null : group;
    }

    public static String getDefaultGroup() {
        return defaultGroup;
    }


}
