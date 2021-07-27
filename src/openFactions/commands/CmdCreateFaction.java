package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdCreateFaction {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		//null check (check if CommandSender is the console)
		if (player == null ) {
			sender.sendMessage("Faction creation as the console is not allowed.");
			return false;
		} else if(Helper.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage(MsgPrefix.ERR + "You are already in a faction."
					+ " You must first leave your faction "
					+ "in order to make a new one.");
			return false;
		} else if (extraArguments.length > 2) {
			sender.sendMessage(MsgPrefix.ERR + "You may not have spaces in your faction name.");
			return false;
		} else if (Helper.doesFactionExist(extraArguments[1])) {
			sender.sendMessage(MsgPrefix.ERR + "You may not create a faction with name " 
					+ extraArguments[1] + " because it already exists.");
			return false;
		}
		
		//get player's unique id
		Faction faction = new Faction(extraArguments[1], player.getUniqueId());
		
		CustomNations.factions.add(faction);
		sender.sendMessage(MsgPrefix.OK + "You have created " + faction.getName() + ".");
		
		Faction.serialize(faction, faction.getAutoFileName());
		
		return true;
	}

}
