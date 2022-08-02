package openFactions.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdLeaveFaction {

	public boolean handle(CommandSender sender) {
		
		Player pl = (Player) sender;
		UUID plUuid = pl.getUniqueId();
		Faction fac = Helper.getPlayerFaction(plUuid);
		
		if (fac != null) {
			fac.removeMember(plUuid);
			pl.sendMessage(MsgPrefix.OK + "You have left " + fac.getName() + ".");
			//if you are the last member of the faction
			//delete the faction
			if ( fac.getMembers().size() < 1 ) {
				pl.sendMessage(MsgPrefix.OK + "You have disbanded "+fac.getName()+".");
				//TODO: add broadcast
				CustomNations.factions.remove(fac);
				CustomNations.deleteFactionSave(fac.getAutoFileName());
				return true;
			}
			
		} else {
			sender.sendMessage(MsgPrefix.ERR + "You are not in a real faction!");
		}
		return false;
	}

}
