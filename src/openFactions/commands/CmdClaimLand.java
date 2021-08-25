package openFactions.commands;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdClaimLand {

	public boolean handle(CommandSender sender, Command command, String[] extraArguments) {
		Player player = Helper.validateCommandSender(sender);
		
		PlayerInfo pi = new PlayerInfo(player);
		
		Faction fac = pi.getPlayerFaction();
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "Land claim failed! You are not in a faction.");
			return false;
		}
		
		//if we are to claim one at a time
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			if(!Helper.doesGroupHavePermission(Can.CLAIM, pi.getPlayerGroup())) {
				sender.sendMessage(MsgPrefix.ERR + "You do not have permission to claim land in your faction.");
				sender.sendMessage(MsgPrefix.OK + "Groups in your faction that can claim: " + Helper.getGroupsByPermission(fac, Can.CLAIM));
				return false;
			}
			
			if ( Helper.isSpecifiedChunkInsideAnyFaction(chunk)  ) {
				//TODO: account for diplomacy
				//TODO: account for contest claiming
				sender.sendMessage(MsgPrefix.ERR + "This territory is already claimed.");
				return false;
			} else {
				fac.addClaim(new LandClaim(chunk));
				sender.sendMessage(MsgPrefix.OK + "You have successfully claimed this chunk.");
				Faction.serialize(fac, fac.getAutoFileName());
			}
			
		}
		
		
		return true;
	}

}
