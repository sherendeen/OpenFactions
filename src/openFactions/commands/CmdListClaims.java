package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.LandClaim;
import openFactions.objects.PlayerInfo;
import openFactions.util.constants.MsgHeader;
import openFactions.util.constants.MsgPrefix;

public class CmdListClaims {

	public boolean handle(CommandSender sender, Player player) {

		PlayerInfo pi = new PlayerInfo(player);
		if(!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You are not a member of a faction. No claimed chunks to list.");
			return false;
		}
		
		if ((pi.getPlayerFaction().getClaims().size() < 1)) {
			sender.sendMessage(MsgPrefix.ERR + "No claimed chunks to list.");
			return false;
		}
		
		sender.sendMessage(MsgHeader.FACTION_CLAIMS);
		for (LandClaim lc : pi.getPlayerFaction().getClaims()) {
			sender.sendMessage(lc.toString());
		}
		return true;
	}

}
