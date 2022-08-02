package openFactions.commands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdAssignToGroup {

	public boolean handle(CommandSender sender, String[] extraArguments) {

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
		
		if ( Helper.doesGroupHavePermission(Can.ASSIGN_GROUPS, group )== false ) {
			sender.sendMessage(MsgPrefix.ERR + "You aren't allowed to assign people to a group.");
			return false;
		} 
		
		Group groupToEdit = Helper.getGroupFromFactionByName(
				extraArguments[1], fac);
		
		UUID uuid = Helper.getUuidFromPlayerName(extraArguments[2]);
		
		if (uuid == null) {
			sender.sendMessage(MsgPrefix.ERR + "Specified player does not exist.");
			return false;
		}
		
		
		if (groupToEdit.getMembers().contains(uuid)) {
			
			sender.sendMessage(MsgPrefix.ERR + "Player is already in group called " 
					+ extraArguments[1] + ".");
			return false;
		}
		group.removeMember(uuid);
		groupToEdit.addMember(uuid);
		
		fac.setGroupAtIndex(fac.getGroups().lastIndexOf(group), group);
		fac.setGroupAtIndex(fac.getGroups().indexOf(groupToEdit), groupToEdit);
		
		return true;
	}

}
