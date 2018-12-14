package me.iblitzkriegi.dexpermissions;

import me.iblitzkriegi.dexpermissions.util.ConfigManager;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class DexPermissions extends JavaPlugin {

    static DexPermissions instance = new DexPermissions();
    public HashMap<String, PermissionAttachment> playerPermissions = new HashMap<>();
    private DexPermissions() {
    }

    public static DexPermissions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setup(this);
    }

    @Override
    public void onDisable() {

    }

}
