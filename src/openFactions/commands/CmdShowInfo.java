package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.PlayerInfo;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdShowInfo {

	public boolean handle(CommandSender sender) {
		
		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		
		sender.sendMessage(MsgPrefix.INFO + "--- Your Information---");
		if (pi.isPlayerInAFaction()) {
			sender.sendMessage(ChatColor.AQUA + "Your faction: " + ChatColor.WHITE + pi.getPlayerFaction().getName());
			sender.sendMessage(ChatColor.AQUA + "Faction description: " + ChatColor.WHITE + pi.getPlayerFaction().getDesc());
			
			// they should be in a group...
			if (pi.isPlayerInAGroup()) {
				sender.sendMessage(ChatColor.AQUA + "Your group/division: " + ChatColor.WHITE+ pi.getPlayerGroup().getName());
			}
			
			// list groups within faction
			Helper.listGroups(pi.getPlayerFaction(), pi.getPlayer());
			
			sender.sendMessage("" +ChatColor.LIGHT_PURPLE + ChatColor.ITALIC + "To view your group specific permissions, type ");
			sender.sendMessage(ChatColor.BLUE + "/" + ChatColor.GREEN + "of showgroup " + ChatColor.GOLD + pi.getPlayerGroup().getName());
			
			
		} else {
			sender.sendMessage(ChatColor.AQUA + "Your faction: " + ChatColor.WHITE + " n/a.");
			sender.sendMessage(ChatColor.AQUA + "Faction description: " + ChatColor.WHITE + " n/a");
			
		}
		
		return true;
	}

}
