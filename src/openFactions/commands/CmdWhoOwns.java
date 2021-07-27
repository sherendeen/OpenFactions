package openFactions.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdWhoOwns {

	public boolean handle(CommandSender sender) {

		Player player = Helper.validateCommandSender(sender);
		
		Chunk chunk = player.getLocation().getChunk();
		
		if ( Helper.isSpecifiedChunkInsideAnyFaction( chunk)) {
			
			ArrayList<Faction> facs = Helper.returnFactionObjectsWhereChunkIsFoundIn(chunk);
			
			sender.sendMessage(MsgPrefix.INFO + "--- Land claim ownership ---");
			for(Faction fac : facs) {
				
				LandClaim lc = Helper.returnLandClaimContainingSpecifiedChunk(chunk);
				
				sender.sendMessage(ChatColor.AQUA + "Claimed by "+ fac.getName() + (  lc.getClaimDescriptor().isEmpty() ? ". " +lc.getClaimDescriptor() : "." ) );
			}
			
			return true;
			
		} else {
			sender.sendMessage(MsgPrefix.INFO + "This land is not claimed by anyone.");
		}
		
		return false;
	}

}
