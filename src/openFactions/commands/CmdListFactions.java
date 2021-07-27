package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import openFactions.CustomNations;
import openFactions.util.constants.MsgPrefix;

public class CmdListFactions {

	public boolean handle(CommandSender sender) {
		sender.sendMessage(MsgPrefix.INFO + "List of factions - Output");
		
		for ( int i = 0 ; i < CustomNations.factions.size(); i++ ) {
			sender.sendMessage(ChatColor.AQUA + CustomNations.factions.get(i).toString());
		}
		return true;
	}

}
