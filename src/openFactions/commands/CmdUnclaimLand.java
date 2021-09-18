package openFactions.commands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdUnclaimLand {

	public boolean handle(CommandSender sender, Command command, String[] extraArguments) {

		Player player = Helper.validateCommandSender(sender);
		
		//get players faction
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		if (fac == null) {
			sender.sendMessage(MsgPrefix.ERR + "You can't do this because you are not in a faction.");
			return false;
		}
		
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			LandClaim lc = Helper.getLandClaimFromChunk(chunk);
			
			if(lc == null) {
				return false;
			}
			
			// we don't risk unclaiming land from another faction
			for (LandClaim landClaim : fac.getClaims()) {
				//because fac.getClaims() is just claims for the faction
				//that the current player is in as a member
				if ( landClaim.equals(lc) ) {
					fac.removeClaim(lc);
					sender.sendMessage(MsgPrefix.OK + "You have unclaimed this land claim.");
					Faction.serialize(fac, fac.getAutoFileName());
					return true;
				}
			}
			
			if ( Helper.isSpecifiedLandClaimInsideAnyFaction(lc)) {
				//TODO: account for diplomacy
				sender.sendMessage(MsgPrefix.INFO + "This territory is owned by a different faction.");
				return false;
			} else {
				fac.addClaim(new LandClaim());
			}
			
		}
		
		return false;

	}

}
