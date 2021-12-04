package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdJoinFaction {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player == null) {
			sender.sendMessage(MsgPrefix.ERR + "The console may not join a faction.");
			return false;
		}
		
		if (Helper.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot join a faction as you are already in one!");
			return false;
		} else {
			for (Faction fac : CustomNations.factions) {
				if (fac.getName().equalsIgnoreCase(extraArguments[1]) && fac.isJoinable()) {
					
					fac.addMember(player.getUniqueId());
					Group def = fac.getDefaultGroup();
					//why remove it and then re-add it?
					fac.removeGroup(def);
					
					def.addMember(player.getUniqueId());
					//because we can't alter a group that 
					//we are actively messing with
					fac.addGroup(def);
					
					sender.sendMessage(MsgPrefix.OK + "You have joined " + fac.getName()+".");
				} else if (!fac.isJoinable()) {
					sender.sendMessage(MsgPrefix.ERR +
							"You cannot join this faction because they require members to be manually added.");
					break;
				}
				
			}
			
		}
		return true;
	}

}
