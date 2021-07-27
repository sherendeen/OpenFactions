package openFactions.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdUnclaimAll {

	public boolean handle(CommandSender sender) {
		Player player = Helper.validateCommandSender(sender);
		//if player is in a faction
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			//added to prevent CME
			ArrayList<LandClaim> claimsList = new ArrayList<LandClaim>();
			
			for (LandClaim lc : claimsList) {
				fac.removeClaim(lc);
			}
			sender.sendMessage(MsgPrefix.INFO + "You have unclaimed all land claims.");
			Faction.serialize(fac, fac.getAutoFileName());
			return true;
		}
		
		return false;
	}

}
