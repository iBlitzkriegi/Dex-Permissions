package me.iblitzkriegi.dexpermissions.util;

import me.iblitzkriegi.dexpermissions.util.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {

    public static String getUniqueId(Player player) {
        return player.getUniqueId().toString();
    }

    public static void sendMessage(CommandSender commandSender, String message) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getPrefix() + message));
    }

}
