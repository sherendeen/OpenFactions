package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.PlayerInfo;
import openFactions.objects.Resolution;
import openFactions.objects.enums.Can;
import openFactions.objects.enums.Cmd;
import openFactions.objects.enums.VoteThreshold;
import openFactions.util.constants.MsgPrefix;

public class CmdPropose {

	public boolean handle(CommandSender sender, String[] extraArguments, Player player) {
		
		// check validity of player
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You are not a member of a faction and can't issue this command.");
			return false;
		}
		
		if (!pi.isPlayerInAGroup() || !pi.isPlayerInAGroup()) {
			sender.sendMessage(MsgPrefix.ERR + "You are not able to issue proposals "
					+ "as you are not in a faction group could do that.");
			return false;
		}
		
		if (!pi.getPlayerGroup().hasPermission(Can.PROPOSE_RESOLUTION)) {
			
			
			sender.sendMessage(MsgPrefix.ERR + "Your group " + ChatColor.AQUA 
					+ "<" + ChatColor.LIGHT_PURPLE 
					+ pi.getPlayerGroup().getName() 
					+ ChatColor.AQUA + ">" + ChatColor.RESET 
					+ " doesn't have permission " 
					+ ChatColor.GOLD+ "[" + ChatColor.RED 
					+ Can.PROPOSE_RESOLUTION.name() 
					+ ChatColor.GOLD+ "]" + ChatColor.RESET
					);
			return false;
		}
		
		// syntax
		// /of propose setrelation war russia
		
		//TODO: check command validity
		
		//create proposal itself
		Faction fac = pi.getPlayerFaction();
		
		System.out.println(extraArguments[0]);//`propose`
		System.out.println(extraArguments[1]);// first arg following
		
		//check argumentation
		StringBuilder sb = new StringBuilder();
		sb.append(extraArguments[1]);
		
		//convert string builder object to string
		String proposalText = sb.toString();
		
		//add the new resolution
		fac.addResolution(
				new Resolution("" + fac.getName().charAt(0) 
				+ fac.getCurrentResolutions().size()+1, 
				proposalText, pi.getPlayerName(), proposalText, VoteThreshold.TwoThirds));
		
		for (Resolution r : fac.getCurrentResolutions()) {
			System.out.println(r.toString());
		}
		
		return true;
	}

}
