package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import openFactions.objects.enums.Cmd;
import openFactions.objects.enums.RelationshipType;
import openFactions.util.constants.MsgPrefix;
import openFactions.util.constants.MsgSeparator;

public class CmdHelp {

	public boolean handle(CommandSender sender, String[] extraArguments) {
//		sender.sendMessage(MsgPrefix.INFO + "--- OpenFactions Commands ---");
//		for (int i = 0; i <  Cmd.values().length; i++) {
//			sender.sendMessage(ChatColor.AQUA + Cmd.values()[i].toString());
//		}
		
		
		/**
		 * 	ADDPERMISSION,
	AP,/**ADDPERMISSION ALIAS
	ASSIGN,
	CLAIM,
	CREATE,
	CREATEGROUP,
	CG,/**alias for CreateGroup
	JOIN,
	LIST,
	LEAVE,
	OWNS,
	REMOVEPERMISSION,
	RP,/**RemovePermission Alias
	SETGROUP,
	SETPERMISSION,
	SP,/** alias for SetPermission
	SETRELATION,
	SHOW,
	SHOWGROUP,
	SHOWRELATIONS,
	UNCLAIM,
	UNCLAIMALL,
	WHOIS,
	SETWARP,
	WARP,
	VOTE,
	PROPOSE,
	INFO
		 */
		if (extraArguments.length < 2 || extraArguments[1].equals("1")) {
			sender.sendMessage(MsgPrefix.INFO +  "--- " +ChatColor.BLUE + ChatColor.BOLD + " OpenFactions Commands #1 + " + ChatColor.RESET + " ---");
			
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "create"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "create a new faction. " + MsgSeparator.COMMAND_USAGE_TEXT+"/of create [arg]");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "claim"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "claim land (a single chunk) for your faction. " 
					+MsgSeparator.COMMAND_USAGE_TEXT+"/of claim");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "unclaim"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "unclaim land from your faction (single chunk). " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of unclaim");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "unclaimall"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "unclaim all faction land. " +MsgSeparator.COMMAND_USAGE_TEXT+"/of unclaimall");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "owns"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "check the ownership of the chunk you are standing on " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of owns");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "info"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "show your player-faction info " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of info");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "show"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "show the information of the specified faction " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of show [arg]");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "list"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "list factions " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of list");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "showrelations"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "show the information of the specified faction " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of showrelations <faction name>");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "whois"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "show the information of the specified player " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of whois <player name>");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "creategroup"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "create subdivision or group within your faction " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of creategroup <name of group>");
		} else if (extraArguments[1].equals("2")) {
			
			sender.sendMessage(MsgPrefix.INFO +  "--- " +ChatColor.BLUE + ChatColor.BOLD + " OpenFactions Commands #2 + " + ChatColor.RESET + " ---");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "removegroup"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "remove subdivision or group within your faction " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of removegroup <name of group>");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "leave"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "leave your faction " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of leave");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "setgroup"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "assign the claim that you are standing on, provided that"
					+ " it is a faction-owned claim, to a group, thereby "
					+ "associating that group with the claim " 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of setgroup <name of group>");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "setrelation"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "set the faction's diplomatic relationship to another faction."
					+ ChatColor.AQUA + " usage: " + ChatColor.LIGHT_PURPLE 
					+"/of setrelation <name of other faction> <relation type>"
					+ ChatColor.GOLD +  " e.g: "+ChatColor.GRAY + ChatColor.ITALIC
					+" ALLY, ENEMY, LORD, VASSAL, NEUTRAL, TRUCE.");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "pillage"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "attempt to overclaim the land that you are on "
					+ "(provided that it is owned by another faction that you are at war with)"
					+ "(takes 5 seconds)"
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of pillage");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "listclaims"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "list all claims of your faction "
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of listclaims");
		} else if (extraArguments[1].equals("3")) {
			sender.sendMessage(MsgPrefix.INFO +  "--- " +ChatColor.BLUE + ChatColor.BOLD + " OpenFactions Commands #3 + " + ChatColor.RESET + " ---");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "removepermission"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "remove permission from faction subgroup or subdivision" 
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of removepermission <name of group> <permission>"
					+ChatColor.GOLD +  " e.g: "+ChatColor.GRAY + ChatColor.ITALIC
					+"ASSIGN_GROUPS, CHANGE_FACTION_DESC, CHANGE_FACTION_NAME, CLAIM, CEDE,"
					+ "DISBAND, EDIT_CLAIM, EDIT_CLAIM_SETTINGS, EDIT_GROUPS, "
					+ "OVERRIDE_CLAIM_SETTINGS, KICK, MAKE_OR_REMOVE_GROUPS, SET_RELATION, "
					+ "SET_VISA, UNCLAIM, UNCLAIM_ALL, "
					+ "PROPOSE_RESOLUTION, "
					+ "USE_FACTION_WARP, OPEN_CONTAINERS");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "addpermission"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "add permission to a specific group within your faction "
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of addpermission <name of group> <permission>");
			
			sender.sendMessage(MsgSeparator.COMMAND_INFO_FIELD_OPEN + "cede"+ MsgSeparator.COMMAND_INFO_FIELD_CLOSE +  " - "
					+ "cede claimed land (that you are standing on) to another faction "
					+ MsgSeparator.COMMAND_USAGE_TEXT+"/of cede <name of faction>");
			
			
			
		}
		
		
		
		return true;
	}

}
