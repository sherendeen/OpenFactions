package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdChangeFactionName {

	public boolean handle(CommandSender sender, String[] extraArguments) {

		Player player = (Player) sender;
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot change faction description because you aren't in one.");
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());

		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.CHANGE_FACTION_NAME, group )== false ) {
			sender.sendMessage(MsgPrefix.ERR +"You aren't allowed to change the faction name.");
			return false;
		} 
		
		if (extraArguments[1] != null) {
			sender.sendMessage(MsgPrefix.OK + 
					"Faction name has been changed to `" 
					+ extraArguments[1] + ".`");
			fac.setName(extraArguments[1]);
		} else {
			return false;
		}
		
		return true;

	}

}
