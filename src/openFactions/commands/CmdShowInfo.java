package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.PlayerInfo;
import openFactions.util.constants.MsgPrefix;

public class CmdShowInfo {

	public boolean handle(CommandSender sender) {
		
		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		
		sender.sendMessage(MsgPrefix.INFO + "--- Your Information---");
		if (pi.isPlayerInAFaction()) {
			sender.sendMessage(ChatColor.AQUA + "Your faction: " + ChatColor.WHITE + pi.getPlayerFaction().getName());
			sender.sendMessage(ChatColor.AQUA + "Faction description: " + ChatColor.WHITE + pi.getPlayerFaction().getDesc());
			
			sender.sendMessage(ChatColor.AQUA + "All groups in your faction: ");
			for (int i = 0 ; i < pi.getPlayerFaction().getGroups().size(); i++) {
				sender.sendMessage(ChatColor.WHITE +""+ (i + 1)+"." + ChatColor.LIGHT_PURPLE + pi.getPlayerFaction().getGroups().get(i).getName());
			}
			
		} else {
			sender.sendMessage(ChatColor.AQUA + "Your faction: " + ChatColor.WHITE + " n/a.");
			sender.sendMessage(ChatColor.AQUA + "Faction description: " + ChatColor.WHITE + " n/a");
			sender.sendMessage(ChatColor.AQUA + "Your group: "+ ChatColor.WHITE + " n/a.");
			
		}
		
		// they should be in a group...
		if (pi.isPlayerInAGroup()) {
			sender.sendMessage(ChatColor.AQUA + "Your group/division: " + ChatColor.WHITE+ pi.getPlayerGroup().getName());
		}
		
		
		return true;
	}

}
