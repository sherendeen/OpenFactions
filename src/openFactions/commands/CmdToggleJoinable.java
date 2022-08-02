package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import openFactions.objects.Faction;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.constants.MsgPrefix;

public class CmdToggleJoinable {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		
		if(!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot set joinability option when you are not in a faction.");
			return true;
		}
		
		Faction fac = pi.getPlayerFaction();
		
		// make sure player is in a group that can toggle joinable
		if (!pi.isPlayerInAGroup() || !pi.getPlayerGroup().hasPermission(Can.TOGGLE_JOINABLE)) {
			sender.sendMessage(MsgPrefix.ERR + "You do not have sufficient permission to toggle joinability option.");
		}
		
		if (extraArguments != null || extraArguments.length < 1) {
			
			
			// toggle switch joinability
			fac.setJoinable(!fac.isJoinable());
			
		}
		
		if (extraArguments != null && extraArguments.length > 0) {
			if (extraArguments[1].equalsIgnoreCase("true")) {
				fac.setJoinable(true);
			} else if (extraArguments[1].equalsIgnoreCase("false")) {
				fac.setJoinable(false);
			} else {
				sender.sendMessage(MsgPrefix.ERR + "Invalid argument.");
				return false;
			}
		}
		
		sender.sendMessage(MsgPrefix.OK + ChatColor.AQUA + "Faction joinability is set to " + ChatColor.RED + extraArguments[1].toUpperCase());
		
		return true;
	}

}
