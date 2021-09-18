package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.PlayerInfo;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdShowRelations {

	
	
	public boolean handle(CommandSender sender, String[] extraArguments) {
		// TODO: don't require all caps for input. Suggestion: use toLower

		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		
		//PlayerInfo pi = new PlayerInfo(player);
		
		if (extraArguments.length < 2) {
			
			for (Faction f : CommandCore.getMain().factions) {
				sender.sendMessage(Faction.getRelationshipString(f));
			}
			
			return true;
		}
		
		if (Helper.getFactionByFactionName(extraArguments[1]) == null) {
			sender.sendMessage(MsgPrefix.ERR + extraArguments[1] + " is not a real faction!");
			return false;
		}

		Faction fac2 = Helper.getFactionByFactionName(extraArguments[1]);
		sender.sendMessage(MsgPrefix.INFO + Faction.getRelationshipString(fac2));

		return true;

	}

}
