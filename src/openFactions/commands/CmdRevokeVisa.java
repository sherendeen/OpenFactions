package openFactions.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Visa;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdRevokeVisa {

	public boolean handle(CommandSender sender, String[] extraArguments) {

		UUID visaHolder = null;
		Player player = (Player) sender;

		// Sender has to specify a player, more specifically, a real one

		if (extraArguments.length < 2) {
			sender.sendMessage(MsgPrefix.ERR + "You must specify a player!");
			return true;
		}

		// If the player is not real, exit.

		try {
			visaHolder = Helper.getUuidFromPlayerName(extraArguments[1]);
		} catch (NullPointerException e) {
			sender.sendMessage(MsgPrefix.ERR + "You must specify a real player!");
			return true;
		}

		if (Helper.isPlayerInAnyFaction(player.getName()) == false) {
			sender.sendMessage(MsgPrefix.ERR + "You are not in a faction!");
			return true;
		}

		Faction senderFaction = Helper.getPlayerFaction(player.getUniqueId());
		ArrayList<Visa> senderFactionVisaList = senderFaction.getVisas();

		if (visaHolder != null) {

			for (int i = 0; i < senderFactionVisaList.size(); i++) {
				Visa v = senderFactionVisaList.get(i);
				if (v.getVisaHolder().equals(visaHolder)) {
					senderFactionVisaList.remove(i);
					sender.sendMessage(MsgPrefix.OK + "Revoked " + extraArguments[1] + "'s visa");
					return true;
				}
			}
			sender.sendMessage(MsgPrefix.ERR + "That player does not have a visa!");
			return true;

		}

		return false;
	}

}
