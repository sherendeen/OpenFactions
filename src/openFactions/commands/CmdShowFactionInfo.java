package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.objects.PlayerInfo;
import openFactions.util.Helper;
import openFactions.util.constants.MsgHeader;
import openFactions.util.constants.MsgPrefix;

public class CmdShowFactionInfo {

	public boolean handle(CommandSender sender, String[] extraArguments) {

		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		
		if (extraArguments == null && pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.INFO + Helper.getPlayerFaction(player.getUniqueId()).toString());
			return true;
		}
		
		if (!Helper.doesFactionExist(extraArguments[1])) {
			return false;
		}
		
		sender.sendMessage(MsgHeader.FACTION_INFO);
		
		//TODO: make more elegant show faction report
		for ( Faction faction1 : CustomNations.factions) {
			//must be bugged
			if (faction1.getName().equalsIgnoreCase(extraArguments[1])) {
				sender.sendMessage(ChatColor.GREEN + "Name: " +ChatColor.WHITE+ faction1.getName());
				sender.sendMessage(ChatColor.GREEN + "Number of members:"  +ChatColor.WHITE+ faction1.getMembers().size());
				sender.sendMessage(ChatColor.GREEN + "Number of groups: " +ChatColor.WHITE+faction1.getGroups().size());
				sender.sendMessage(ChatColor.GREEN + "Number of faction warps:" +ChatColor.WHITE+ faction1.getWarps().size());
				sender.sendMessage(ChatColor.GREEN + "Number of territories (chunks): "+ChatColor.WHITE + faction1.getClaims().size());
				sender.sendMessage(ChatColor.GREEN + "Default group:" +ChatColor.WHITE+ faction1.getDefaultGroup().getName());
			}
		}
		
		return true;
	}

}
