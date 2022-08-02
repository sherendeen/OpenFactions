package openFactions.commands;

import java.time.Period;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdCreateGroup {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		/// cast
		Player player = (Player) sender;
		
		if (player == null) { 
			sender.sendMessage(MsgPrefix.ERR + "You cannot issue this command as console.");
			return false; 
		}
		
		if (extraArguments.length < 2) {
			sender.sendMessage(MsgPrefix.ERR + "Insufficient number of arguments.");
			return false;
		}
		
		
		
		//determine if player is in a faction
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			if (Helper.doesGroupExist( extraArguments[1], fac)) {
				sender.sendMessage(MsgPrefix.ERR + "This group of " + extraArguments[1] + " already exists!" );
				return false;
			}
			
			Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
			
			if (Helper.doesGroupHavePermission(Can.MAKE_OR_REMOVE_GROUPS, group)) {
				//inherit from the group you are in but have a different name
				
				ArrayList<Can> perms = new ArrayList<Can>();
				perms.addAll(group.getGroupPermissions());
				int max = group.getMaxMembers();
				boolean isJoinable = group.isJoinable();
				Period pp = group.getTerm();
				
				Group newGroup = new Group(extraArguments[1], 
						isJoinable,
						pp,
						false,
						max,
						perms);
				
				//Group(String name, ArrayList<UUID> members, boolean joinable, Period term, boolean termsEnd,
				//	int maxMembers, Can... groupPermissions)
				//newGroup = Group.removeAllMembersFromGroup(newGroup);
				// we don't want to inherit the members of the group
				
				fac.addGroup(newGroup);
				
				sender.sendMessage(MsgPrefix.OK + "New group ["+extraArguments[1]+"] created.");
				Faction.serialize(fac, fac.getAutoFileName());
			}
			
		} else {
			return false;
		}
		
		
		return true;
	}

}
