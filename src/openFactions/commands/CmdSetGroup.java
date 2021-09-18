package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.LandClaim;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdSetGroup {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		
		Player player = null;
		if (sender instanceof Player) {
			player =  (Player)sender;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if(!pi.isPlayerInAFaction()) {	
			sender.sendMessage(MsgPrefix.ERR + "You cannot issue this command."
					+ " you are not in a faction." );
			return false;
		}
		
		if (!pi.isPlayerInAGroup()) {
			sender.sendMessage(MsgPrefix.ERR + "You are not in a group.");
			return false;
		}
		
		if(!pi.getPlayerGroup().hasPermission(Can.EDIT_CLAIM_SETTINGS) &&
				!pi.getPlayerGroup().hasPermission(Can.OVERRIDE_CLAIM_SETTINGS)) {
			
			sender.sendMessage(MsgPrefix.ERR + "You do not have permission to edit claim settings.");
			return false;
		}
		
		if(extraArguments.length != 2) {
			return false;
		}
		
		if (!Helper.doesGroupExist(extraArguments[1], pi.getPlayerFaction() )) {
			sender.sendMessage(MsgPrefix.ERR + "Group [" + extraArguments[1] + "] does not exist.");
			return false;
		}
		
		Group group = Helper.getGroupFromFactionByName(extraArguments[1], pi.getPlayerFaction());
		LandClaim landClaim = Helper.getLandClaimFromChunk(player.getLocation().getChunk());
		
		landClaim.setExclusiveGroup(group);
		
		// serialize
		Faction.serialize(pi.getPlayerFaction(), pi.getPlayerFaction().getAutoFileName());
		
		return true;
	}

}
