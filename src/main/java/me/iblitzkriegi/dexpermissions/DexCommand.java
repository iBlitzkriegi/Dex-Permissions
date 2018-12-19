package me.iblitzkriegi.dexpermissions;

import me.iblitzkriegi.dexpermissions.util.Util;
import me.iblitzkriegi.dexpermissions.util.managers.ConfigManager;
import me.iblitzkriegi.dexpermissions.util.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DexCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] args) {
        if (!commandSender.hasPermission("dex.permissions")) {
            Util.sendMessage(commandSender, "You must have the &adex.permissions &fpermission to use this command!");
            return true;
        }
        if (args.length == 0) {
            sendHelpMessage(commandSender);
            return true;
        }
        String argument = args[0].toLowerCase();
        switch (argument) {
            case "reload":
                ConfigManager.getInstance().reloadConfig();
                Util.sendMessage(commandSender, "The config has been successfully reloaded");
                break;
            case "group":
                if (args.length <= 1) {
                    sendHelpMessage(commandSender);
                    return true;
                }
                String firstArgument = args[1];
                if (firstArgument.equalsIgnoreCase("set")) {
                    if (args.length != 4) {
                        sendHelpMessage(commandSender);
                        return true;
                    }
                    Player player = Bukkit.getServer().getPlayer(args[2]);
                    if (player == null) {
                        Util.sendMessage(commandSender, "Could not parse &a" + args[2] + " &fas a player.");
                        return true;
                    }
                    PermissionManager.setGroup(player, args[3]);
                    return true;
                } else if (firstArgument.equalsIgnoreCase("create")) {
                    if (args.length != 3) {
                        sendHelpMessage(commandSender);
                        return true;
                    }
                    PermissionManager.createGroup(args[2]);
                    return true;
                }
                ConfigurationSection groupSection = ConfigManager.getInstance().getConfig().getConfigurationSection("Groups." + args[1]);
                if (groupSection == null) return true;

                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("permissions")) {
                        Util.sendMessage(commandSender, "Here is a list of " + args[1] + "'s permissions; " + "");
                        //TODO
                    }
                    return true;
                } else if (args.length >= 5) {
                    String group = args[1];
                    if (args[2].equalsIgnoreCase("set")) {
                        if (args[3].equalsIgnoreCase("prefix")) {
                            PermissionManager.setGroupPrefix(group, getArgs(args, 4));
                            return true;
                        } else if (args[3].equalsIgnoreCase("suffix")) {
                            PermissionManager.setGroupSuffix(group, getArgs(args, 4));
                        }
                        return true;
                    }
                    if (!args[2].equalsIgnoreCase("permission")) {
                        sendHelpMessage(commandSender);
                        return true;
                    }
                    if (args[3].equalsIgnoreCase("add")) {
                        PermissionManager.addPermission(args[1], args[4]);
                        return true;
                    } else if (args[3].equalsIgnoreCase("remove")) {
                        PermissionManager.removePermission(args[1], args[4]);
                        return true;
                    }
                }


            default:
                sendHelpMessage(commandSender);
        }

        return true;
    }

    private void sendHelpMessage(CommandSender player) {
        //TODO this
    }

    public String getArgs(String[] args, int num){
        StringBuilder sb = new StringBuilder();
        for(int i = num; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

}
