package openFactions.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.events.LandPillageEvent;
import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdPillage {
	public boolean handle(CommandSender sender, Command command) {
		
		Player senderAsPlayer = (Player) sender;
		UUID senderUUID = senderAsPlayer.getUniqueId();
		Chunk senderChunk = senderAsPlayer.getLocation().getChunk();
		LandClaim claimedLand = Helper.returnLandClaimContainingSpecifiedChunk(senderChunk);
		Faction senderFaction = Helper.getPlayerFaction(senderUUID);
		Faction landClaimFaction = Helper.returnFactionObjectsWhereChunkIsFoundIn(senderChunk).get(0);
		
		LandPillageEvent pillageEvent = new LandPillageEvent(senderFaction, landClaimFaction, claimedLand);
		
		Bukkit.getServer().getPluginManager().callEvent(pillageEvent);
		
		senderAsPlayer.sendMessage(MsgPrefix.INFO + " Attempting to pillage: " + senderChunk.getX() + "," + senderChunk.getZ() + " from " + landClaimFaction.getName());
		
		if(pillageEvent.getResult() == false) {
			senderAsPlayer.sendMessage("succ");
			return true;
		}
		
		if(pillageEvent.getResult() == true) {
			senderAsPlayer.sendMessage("no succ");
			return true;
		}
		
		
		
		return false;
		
	}
}
