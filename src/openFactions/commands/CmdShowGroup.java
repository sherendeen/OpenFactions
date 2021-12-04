package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.PlayerInfo;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdShowGroup {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		
		if (player == null) { 
			sender.sendMessage(MsgPrefix.ERR +"You cannot issue this command as console.");
			return false; 
		}
		
		if (extraArguments.length < 2 && pi.isPlayerInAFaction()) {
			Helper.listGroups(pi.getPlayerFaction(), pi.getPlayer());
			return true;
		}
		
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
				sender.sendMessage(MsgPrefix.ERR + "This group of " + extraArguments[1] + " does NOT exist!" );
				return false;
			}
			
			sender.sendMessage(MsgPrefix.INFO + "--- Group Information ---");
			Group group = Helper.getGroupFromFactionByName(extraArguments[1], fac);
			sender.sendMessage(ChatColor.AQUA + "Group name: " + ChatColor.WHITE + group.getName());
			sender.sendMessage(ChatColor.AQUA + "Number of members: " + ChatColor.WHITE + group.getMembers().size());
			sender.sendMessage(ChatColor.AQUA + "Is this the default group? " + ((fac.getDefaultGroup().equals(group)) ? ChatColor.GREEN + "Yes": ChatColor.RED + "No"));
//			sender.sendMessage(ChatColor.AQUA + "Group permissions: " +ChatColor.RESET+ group.getGroupPermissions().toString());
			sender.sendMessage(""+ChatColor.ITALIC + ChatColor.GRAY+  "[");
			final int n = pi.getPlayerGroup().getGroupPermissions().size();
			
			for (int i = 0 ; i < n; i++) {
				sender.sendMessage(ChatColor.BLUE + 
						pi.getPlayerGroup().getGroupPermissions().get(i).name() 
						+ ((i==(n-1)) ? "" + ChatColor.WHITE + "," : "")
						);
				
			}
			sender.sendMessage(""+ChatColor.ITALIC + ChatColor.GRAY+  "]");
		}
		
		return true;
	}

	

}
