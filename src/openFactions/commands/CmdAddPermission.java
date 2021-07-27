package openFactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdAddPermission {

	/**
	 * 
	 * @param sender
	 * @param command
	 * @param extraArguments
	 * @return
	 */
	public boolean handle(CommandSender sender, Command command, String[] extraArguments) {
		Player player = (Player) sender;
		
		if(!Helper.isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		
		if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage(MsgPrefix.ERR + "This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( !Helper.doesGroupHavePermission(Can.EDIT_GROUPS, group) ) {
			sender.sendMessage(MsgPrefix.ERR + "You aren't allowed to edit this particular group.");
			return false;
		} 
		
		Group groupToEdit = Helper.getGroupFromFactionByName(extraArguments[1], fac);
		
		if (!Helper.doesStringMatchAValidPermission(extraArguments[2])) {
			sender.sendMessage(MsgPrefix.ERR + "Invalid permission: " +extraArguments[2] + ". Try /of help");
			return false;
		}
		
		if (groupToEdit.hasPermission(Can.valueOf(extraArguments[2]))) {
			sender.sendMessage(MsgPrefix.ERR + "This permission has already been added.");
			return false;
		}
		
		//not sure if it is totally necessary to do this
		
		
		groupToEdit.addPermission(Can.valueOf(extraArguments[2]));
		
		fac.setGroupAtIndex(fac.getGroups().indexOf(groupToEdit), groupToEdit);
		
		Faction.serialize(fac, fac.getAutoFileName());
		
		return true;
	}

}
