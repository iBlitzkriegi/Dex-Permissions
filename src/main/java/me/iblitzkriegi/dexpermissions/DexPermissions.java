package me.iblitzkriegi.dexpermissions;

import me.iblitzkriegi.dexpermissions.util.managers.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import static me.iblitzkriegi.dexpermissions.util.managers.PermissionManager.playerPermissions;

public class DexPermissions extends JavaPlugin {

    private static DexPermissions instance;

    public DexPermissions() {
        instance = this;
    }

    public static DexPermissions getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setup(this);
        getServer().getPluginManager().registerEvents(new EvtJoinLeave(), this);
        this.getCommand("dex").setExecutor(new DexCommand());

    }

    @Override
    public void onDisable() {
        ConfigManager.getInstance().saveConfig();
        playerPermissions.clear();
    }

}
