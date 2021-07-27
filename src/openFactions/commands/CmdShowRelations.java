package openFactions.commands;

import org.bukkit.command.CommandSender;

import openFactions.objects.Faction;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdShowRelations {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		// TODO: don't require all caps for input. Suggestion: use toLower

		if (Helper.getFactionByFactionName(extraArguments[1]) == null) {
			sender.sendMessage(MsgPrefix.ERR + extraArguments[1] + " is not a real faction!");
			return false;
		}

		Faction fac2 = Helper.getFactionByFactionName(extraArguments[1]);
		sender.sendMessage(MsgPrefix.INFO + Faction.getRelationshipString(fac2));

		return true;

	}

}
