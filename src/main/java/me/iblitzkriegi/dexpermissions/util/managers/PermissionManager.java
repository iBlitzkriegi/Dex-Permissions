package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.DexPermissions;
import me.iblitzkriegi.dexpermissions.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

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
        String path = "Users." + uuid + ".permissions";
        List<String> permissions = config.getStringList(path);
        if (permission.isEmpty()) {
            List<String> setPermissions = new ArrayList<>();
            setPermissions.add(permission);
            config.set(path, setPermissions);
        } else {
            if (permissions.contains(permission)) {
                return;
            }
            permissions.add(permission);
            config.set(path, permissions);
        }
    }

}
