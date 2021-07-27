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
			sender.sendMessage(ChatColor.AQUA + "Your faction: " + pi.getPlayerFaction().getName());
			sender.sendMessage(ChatColor.AQUA + "Faction description: " + pi.getPlayerFaction().getDesc());
			
			sender.sendMessage(ChatColor.AQUA + "All groups in your faction:");
			for (int i = 0 ; i < pi.getPlayerFaction().getGroups().size(); i++) {
				sender.sendMessage(i+1+"." + pi.getPlayerFaction().getGroups().get(i).getName());
			}
			
		} else {
			sender.sendMessage(ChatColor.AQUA + "Your faction: n/a.");
			sender.sendMessage(ChatColor.AQUA + "Faction description: n/a");
			sender.sendMessage(ChatColor.AQUA + "Your group: n/a.");
			
		}
		// they should be in a group...
		if (pi.isPlayerInAGroup()) {
			sender.sendMessage(ChatColor.AQUA + "Your group/division: " + pi.getPlayerGroup().getName());
//			sender.sendMessage("Your permissions: " 
//					+ Helper.formatPermissionsArrayToString(pi.getPlayerGroup().getGroupPermissions()));
		}
		
		
		return true;
	}

}
