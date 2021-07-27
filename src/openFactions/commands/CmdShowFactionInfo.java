package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdShowFactionInfo {

	public boolean handle(CommandSender sender, String[] extraArguments) {

		Player player = (Player) sender;
		
		if (extraArguments == null) {
			sender.sendMessage(MsgPrefix.INFO + Helper.getPlayerFaction(player.getUniqueId()).toString());
			return true;
		}
		
		if (!Helper.doesFactionExist(extraArguments[1])) {
			return false;
		}
		//TODO: make more elegant show faction report
		for ( Faction faction1 : CustomNations.factions) {
			//must be bugged
			if (faction1.getName().equalsIgnoreCase(extraArguments[1])) {
				sender.sendMessage(MsgPrefix.INFO + faction1.toString());
			}
		}
		
		return true;
	}

}
