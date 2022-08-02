package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class OFCmdDiplomacy {

    public boolean welcome(CommandSender sender, Command cmd, String label, String[] args) {
    	sender.sendMessage(ChatColor.DARK_AQUA + "Welcome");
    	return true;
    }
	
}
