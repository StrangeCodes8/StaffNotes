package com.staffnotes.Listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.staffnotes.commands.Commands.*;

public class CmdExecuter implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("notes")) {
            if(!sender.hasPermission("notes.default")) return true;
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "add":
                    if (!sender.hasPermission("notes.add")) {
                        sender.sendMessage("§cYou do not have permissions to run this command");
                        return true;
                    } else {
                        return handleAddCommand((Player) sender, args,sender.hasPermission("notes.add.neverplayed"));
                    }
                case "get":
                    if (!sender.hasPermission("notes.view")) {
                        sender.sendMessage("§cYou do not have permissions to run this command");
                        return true;
                    } else {
                        if (args.length == 1 || (args.length == 2 && args[1].equalsIgnoreCase("all"))){
                            if (!sender.hasPermission("notes.view.all")) {
                                sender.sendMessage("§cPlease enter a PlayerName");
                                return true;
                            } else {
                                return handleGetCommand((Player) sender);
                            }
                        } else if (args.length >= 2){
                            return handleGetCommand((Player) sender,args, sender.hasPermission("notes.view.neverplayed"));
                        } else {
                            return false;
                        }
                    }
                case "remove":
                    if (!sender.hasPermission("notes.remove")) {
                        sender.sendMessage("§cYou do not have permissions to run this command");
                        return true;
                    } else {
                        return handleRemoveCommand((Player) sender, args, sender.hasPermission("notes.remove.neverplayed"));
                    }
                default:
                    return false;
            }
        }
        return false;
    }
}
