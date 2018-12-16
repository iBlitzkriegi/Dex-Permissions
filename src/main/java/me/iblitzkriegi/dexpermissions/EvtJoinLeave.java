package me.iblitzkriegi.dexpermissions;

import me.iblitzkriegi.dexpermissions.util.Util;
import me.iblitzkriegi.dexpermissions.util.managers.PermissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EvtJoinLeave implements Listener {

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("break.block")) {
            event.getPlayer().sendMessage("abad");
            event.setCancelled(true);
        }
        event.getPlayer().sendMessage("good");

    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        PermissionManager.setupPermissions(event.getPlayer());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        PermissionManager.playerPermissions.remove(Util.getUniqueId(event.getPlayer()));

    }

}

