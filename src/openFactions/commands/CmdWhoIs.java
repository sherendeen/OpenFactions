package openFactions.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import openFactions.objects.Faction;
import openFactions.objects.Visa;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdWhoIs {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		UUID uuid = Helper.getUuidFromPlayerName(extraArguments[1]);
        System.out.println("OFDB: whoisReport: arg0:[" + extraArguments[0] + "], arg1:[" + extraArguments[1] + "]");
        if (uuid == null) {
            sender.sendMessage(MsgPrefix.ERR + String.valueOf(extraArguments[1]) + " does not exist.");
            return false;
        }
        
        if ( Helper.isPlayerInAnyFaction(extraArguments[1])) {
        	
	        Faction fac = Helper.getPlayerFaction(uuid);
	        String playerName = Helper.getPlayerNameFromUuid(uuid);
	        
	        sender.sendMessage(MsgPrefix.INFO + "--- Who Is " + playerName + "? Report ---");
	        
	        sender.sendMessage(MsgPrefix.INFO + String.valueOf(playerName) + " is a member of the faction called " + fac.getName() + ".");
	        
	        sender.sendMessage(MsgPrefix.INFO + "They are in the group called " + Helper.getGroupPlayerIsIn(fac, uuid).getName() + ".");
        } 
        
        ArrayList<Visa> visasThatThePlayerHas = Helper.getVisasOfPlayer(uuid);
        
        if (visasThatThePlayerHas.size() > 0) {
            sender.sendMessage("- Visas -");
            for (Visa visa : visasThatThePlayerHas) {
                String[] fields = Helper.getVisaReport(visa);
                for (int i = 0; i < fields.length; ++i) {
                    sender.sendMessage(MsgPrefix.INFO + fields[i]);
                }
            }
            sender.sendMessage(MsgPrefix.INFO + "");
        }
        
        sender.sendMessage(MsgPrefix.ERR + extraArguments[1] + " is not in a faction.");
        return true;
        
	}

}
