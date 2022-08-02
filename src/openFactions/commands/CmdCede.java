package openFactions.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.LandClaim;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdCede {

	public boolean handle(CommandSender sender, Command command, String[] extraArguments) {
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if(!pi.isPlayerInAFaction() || !pi.isPlayerInAGroup()) {
			sender.sendMessage(MsgPrefix.ERR + "You do not have membership requirement to issue this command.");
			return false;
		}
		
		
		if(!pi.getPlayerGroup().hasPermission(Can.CEDE)) {
			sender.sendMessage(MsgPrefix.ERR + "You lack group permission " + ChatColor.LIGHT_PURPLE + "" + Can.CEDE.toString() + ChatColor.RESET + ".");
			return false;
		}
		
		//SETUP CEDE
		//TEST FOR ARGS
		if (extraArguments.length == 3) {
			
			for (String s : extraArguments) {
				if (s == null || s.equals("")) {
					return false;
				}
			}
			
			if (!Helper.doesFactionExist(extraArguments[1])) {
				sender.sendMessage(MsgPrefix.ERR + "Faction " 
						+ ChatColor.LIGHT_PURPLE + "" + extraArguments[1]
								+ ChatColor.RESET + " does not exist.");
				return false;
			}
			
			if (!Helper.doesGroupExist(extraArguments[2], pi.getPlayerFaction()
					)) {
				sender.sendMessage(MsgPrefix.ERR + "Group " 
						+ ChatColor.LIGHT_PURPLE + "" + extraArguments[2]
								+ ChatColor.RESET + " does not exist.");
				return false;
			}
			
			Faction recipientFaction = Helper.getFactionByFactionName(extraArguments[1]);
			Group group = Helper.getGroupFromFactionByName(extraArguments[2], 
					pi.getPlayerFaction());
			
			ArrayList<LandClaim> territories = new ArrayList<LandClaim>();
			
			// select territory
			for (LandClaim lc : pi.getPlayerFaction().getClaims()) {
				if (lc.getExclusiveGroup().equals(group)) {
					LandClaim landClaim = lc;
					// hopefully this won't cause a crash!
					pi.getPlayerFaction().removeClaim(lc);
					territories.add(landClaim);
					
				}
			}
			
			// actually transfer the territories
			for (LandClaim lc : territories) {
				recipientFaction.addClaim(lc);
			}
			
			sender.sendMessage(MsgPrefix.INFO + "Ceded " +ChatColor.BOLD + territories.size() +
					ChatColor.RESET +" " + ChatColor.AQUA + group.getName()
			+ChatColor.RESET +" claim(s) to faction " +ChatColor.LIGHT_PURPLE 
			+ recipientFaction.getName() + ChatColor.RESET + ".");
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(MsgPrefix.INFO + pi.getPlayerFaction().getName() + " ceded " + territories.size() +
						" land claim(s) to " + recipientFaction.getName() + ".");
			}
			
			
		} else if (extraArguments.length == 2) {
			
			if (!Helper.doesFactionExist(extraArguments[1])) {
				sender.sendMessage(MsgPrefix.ERR + "Faction " 
						+ ChatColor.LIGHT_PURPLE + "" + extraArguments[1]
								+ ChatColor.RESET + " does not exist.");
				return false;
			}
			
			if(!Helper.isSpecifiedChunkInsideAnyFaction(player.getLocation().getChunk())) {
				sender.sendMessage(MsgPrefix.ERR +"You cannot cede land that you do not own!");
				return false;
			}
			
			Faction recipientFaction = Helper.getFactionByFactionName(extraArguments[1]);
			LandClaim lc = Helper.getLandClaimFromChunk(player.getLocation().getChunk());
			pi.getPlayerFaction().removeClaim(lc);
			recipientFaction.addClaim(lc);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendMessage(MsgPrefix.INFO + pi.getPlayerFaction().getName() + " ceded one territory to " + recipientFaction.getName() + ".");
			}
		}
		
		return true;
	}

}
