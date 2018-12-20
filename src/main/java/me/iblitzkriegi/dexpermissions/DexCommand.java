package me.iblitzkriegi.dexpermissions;

import me.iblitzkriegi.dexpermissions.util.Util;
import me.iblitzkriegi.dexpermissions.util.managers.ConfigManager;
import me.iblitzkriegi.dexpermissions.util.managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
            sendInvalidUsage(commandSender);
            return true;
        } else if (args.length == 1) {
            String category = args[0].toLowerCase();
            if (!category.contains("reload")) {
                if (!(category.contains("group") || category.contains("user"))) {
                    sendInvalidUsage(commandSender);
                    return true;
                }
                sendHelpMessage(commandSender, category);
                return true;
            }
        }
        String argument = args[0].toLowerCase();
        switch (argument) {
            case "reload":
                ConfigManager.getInstance().reloadConfig();
                Util.sendMessage(commandSender, "The config has been successfully reloaded");
                break;
            case "group":
                if (args.length <= 1) {
                    sendInvalidUsage(commandSender);
                    return true;
                }
                String firstArgument = args[1];
                if (firstArgument.equalsIgnoreCase("set")) {
                    if (args.length != 4) {
                        sendInvalidUsage(commandSender);
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
                    if (args.length < 3) {
                        sendInvalidUsage(commandSender);
                        return true;
                    } else if (args.length == 4) {
                        //TODO INHERITS!
                    }
                    PermissionManager.createGroup(args[2]);
                    return true;
                } else if (firstArgument.equalsIgnoreCase("delete")) {
                    if (args.length != 3) {
                        sendInvalidUsage(commandSender);
                        return true;
                    }
                    PermissionManager.deleteGroup(args[2]);
                }
                ConfigurationSection groupSection = ConfigManager.getInstance().getConfig().getConfigurationSection("Groups." + args[1]);
                if (groupSection == null) {
                    Util.sendMessage(commandSender, "Could not parse &a" + args[1] + " &fas a valid group.");
                    return true;
                }

                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("permissions")) {
                        Util.sendMessage(commandSender, "Here is a list of " + args[1] + "'s permissions; " + "");
                        return true;
                    }
                    sendInvalidUsage(commandSender);
                    return true;
                } else if (args.length >= 4) {
                    String group = args[1];
                    if (args[2].equalsIgnoreCase("set")) {
                        String arg = args[3].toLowerCase();
                        switch (arg) {
                            case "prefix":
                                PermissionManager.setGroupPrefix(group, getArgs(args, 4));
                                break;
                            case "suffix":
                                PermissionManager.setGroupSuffix(group, getArgs(args,4));
                                break;
                            case "default":
                                PermissionManager.setDefaulGroup(group);
                                break;
                            default:
                                sendInvalidUsage(commandSender);

                        }
                        return true;
                    }
                    if (args[3].equalsIgnoreCase("permission")) {
                        String arg = args[2].toLowerCase();
                        switch (arg) {
                            case "add":
                                PermissionManager.addPermission(group, args[4]);
                                break;
                            case "remove":
                            case "delete":
                                PermissionManager.removePermission(group, args[4]);
                                break;
                            default:
                                sendInvalidUsage(commandSender);
                        }
                        return true;
                    }
                }


            default:
                sendInvalidUsage(commandSender);
        }

        return true;
    }

    private void sendHelpMessage(CommandSender commandSender, String type) {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("&b&l------- &fDex-Permissions &b&l-------------------\n");
        if (type.contains("group")) {
            helpMessage.append("&8Use /dex group <argument> to use each listed item\n");
            helpMessage.append("&bset <user> <group> - &fSet a users group\n");
            helpMessage.append("&bcreate <name> - &fCreate a new group\n");
            helpMessage.append("&bdelete <name> - &fDelete a group\n");
            helpMessage.append("&b<group> <permissions> - &fGet a groups perms\n");
            helpMessage.append("&b<group> set prefix <prefix> - &fSet chat prefix\n");
            helpMessage.append("&b<group> set suffix <suffix> - &fSet chat suffix\n");
            helpMessage.append("&b<group> add permission <perm> - &fAdd perms\n");
            helpMessage.append("&b<group> remove permission <perm> - &fRemove perms\n");
        } else if (type.contains("user")) {

        }
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage.toString()));
    }

    private void sendInvalidUsage(CommandSender commandSender) {
        Util.sendMessage(commandSender, "Incorrect usage! Use /dex <group|user>");
    }

    public String getArgs(String[] args, int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = num; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

}
