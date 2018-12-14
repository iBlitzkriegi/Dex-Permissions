package me.iblitzkriegi.dexpermissions.util.managers;

import me.iblitzkriegi.dexpermissions.DexPermissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;

public class PermissionManager {

    public static HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();

    public static void setupPermissions(Player player) {
        DexPermissions dexPermissions = DexPermissions.getInstance();
        PermissionAttachment permissionAttachment = player.addAttachment(dexPermissions);
    }

}
