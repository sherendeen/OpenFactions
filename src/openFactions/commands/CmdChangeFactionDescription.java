package openFactions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdChangeFactionDescription {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());

		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.CHANGE_FACTION_DESC, group )== false ) {
			sender.sendMessage(MsgPrefix.ERR + "You aren't allowed to change the faction description.");
			return false;
		}  else {
			
			String newDescription = "";
			
			for ( int i = 0 ; i < 0; i ++) {
				System.out.println(extraArguments[i]);
				newDescription +=" "+ extraArguments[i];
			}
			
			fac.setDesc(newDescription);
			
			sender.sendMessage(MsgPrefix.OK + "Faction description is now: ");
			sender.sendMessage(newDescription);
			
			return true;
		}

	}
}