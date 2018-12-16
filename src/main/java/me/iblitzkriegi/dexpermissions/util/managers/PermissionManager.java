package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.DexPermissions;
import me.iblitzkriegi.dexpermissions.util.Util;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

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
                System.out.println(group);
                for (String permission : config.getConfigurationSection("Groups." + group).getStringList("permissions")) {
                    permissionAttachment.setPermission(permission, true);
                }
            }
        }
    }

}
