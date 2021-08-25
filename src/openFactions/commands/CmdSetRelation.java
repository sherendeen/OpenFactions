package openFactions.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.PlayerInfo;
import openFactions.objects.enums.RelationshipType;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdSetRelation {

	public boolean handle(CommandSender sender, String[] extraArguments, boolean willExecute) {

		Player player = (Player) sender;
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (player==null) {
			sender.sendMessage(MsgPrefix.ERR + "The console may not set faction relations within a faction.");
			return false;
		}
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR +"You are not in a faction!");
			return false; 
		}
		
		Faction faction1 = Helper.getPlayerFaction(player.getUniqueId()); 
		
		if(extraArguments[1].equalsIgnoreCase(faction1.getName()) || Helper.getFactionByFactionName(faction1.getName()) == null) {
			sender.sendMessage(MsgPrefix.ERR + "That faction name is invalid!");
			return false;
		}
		
		// execution. probably a bodge
		if (willExecute) {
			execute(extraArguments, faction1);
		}
		
		return true;
	}

	public void execute(String[] extraArguments, Faction faction1) {
		String faction1Name = faction1.getName(); 
		faction1.setRelationshipByFactionName(faction1Name, extraArguments[1], RelationshipType.valueOf(extraArguments[2]));
		Bukkit.broadcastMessage(MsgPrefix.INFO + faction1Name + "declared that they are now an " + extraArguments[2].toUpperCase() + " to " + extraArguments[1]);
	}

}
