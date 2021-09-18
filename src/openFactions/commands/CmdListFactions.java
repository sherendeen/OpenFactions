package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import openFactions.CustomNations;
import openFactions.util.constants.MsgPrefix;

public class CmdListFactions {
	
	private CustomNations main = CommandCore.getMain();

	public boolean handle(CommandSender sender) {
		sender.sendMessage(MsgPrefix.INFO + "List of factions - Output");
		
//		for ( int i = 0 ; i < CustomNations.factions.size(); i++ ) {
//			sender.sendMessage(ChatColor.AQUA + CustomNations.factions.get(i).toString());
//		}
		// not sure if this really fixes the problem
		for (int i = 0 ; i < main.factions.size(); i++) {
			sender.sendMessage(ChatColor.AQUA + CommandCore.getMain().factions.get(i).toString());
		}
		
		return true;
	}

}
