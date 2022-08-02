package openFactions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdAddMember {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		
		Player player = null;
		Player commandInitiator = (Player) sender;
		PlayerInfo pi = new PlayerInfo(commandInitiator);
		
		
		// is extraArguments[1] a player?
		// if not, make player equal to the the result
		if (Helper.getUuidFromPlayerName(extraArguments[1])!=null) {
			player = Bukkit.getPlayer(Helper.getUuidFromPlayerName(extraArguments[1]));
		} else {
			sender.sendMessage(MsgPrefix.ERR + 
					"Player by this name is not online.");
			return false;
		}
		
		// is this player in a faction already?
		if (Helper.isPlayerInAnyFaction(extraArguments[1])) {
			sender.sendMessage(MsgPrefix.ERR + 
					"You cannot add a member who is already in a faction."
					+ " They have to leave theirs first.");
			return false;
		} else if (pi.isPlayerInAFaction() && pi.isPlayerInAGroup() &&
				pi.getPlayerGroup().hasPermission(Can.ADD_PLAYERS)){
			
			// debug
			System.out.println(MsgPrefix.DEBUG + "commandInitiator group: " +
					pi.getPlayerGroup().getName() + " possesses permission ADD_PLAYERS" );
			// get faction of command initator
			// i.e: the user attempting to add a member
			Faction fac = pi.getPlayerFaction();
			// add new member
			fac.addMember(player.getUniqueId());
			Group def = fac.getDefaultGroup();
			//why remove it and then re-add it?
			fac.removeGroup(def);
			
			def.addMember(player.getUniqueId());
			//because we can't alter a group that 
			//we are actively messing with... apparently
			fac.addGroup(def);
		}
		
		return true;
	}

}
