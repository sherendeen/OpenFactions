//Copyright 2018, 2019, 2020, 2021
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.

package openFactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.LandClaim;
import openFactions.objects.PlayerInfo;
import openFactions.objects.Visa;
import openFactions.objects.Warp;
import openFactions.objects.enums.Can;
import openFactions.objects.enums.Cmd;
import openFactions.objects.enums.RelationshipType;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class Commands implements CommandExecutor{
	
	CustomNations plugin;
	public Commands(CustomNations plugin) {
		this.plugin = plugin;
	}
	
	// for teleportation
	private HashMap<String, Long> coolDowns = new HashMap<String, Long>();
	private final int COOLDOWN_TIME = 90;// TODO: Make Configurable
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] extraArguments) {
		
		Player player ;
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		if (!command.getName().equalsIgnoreCase("of")) {
			return false;
		}
		
		if (extraArguments.length < 1) {
			return false;
		}
	
		dateFormat.setLenient(false);
		// check subcommands
		switch (extraArguments[0].toLowerCase()) {
		case "addpermission":
		case "ap":
			
			return addPermissionHandler(sender, command, extraArguments);
		case "assign":
			
			return assignToGroup(sender, extraArguments);
			
		case "claim":
			//Keeping things neat by putting the bulk of the code outside this case block
			return claimLand(sender,command,extraArguments);
			
		case "create":
			
			return createFaction(sender, extraArguments);
			
		case "creategroup":
		case "cg":
			return createGroup(sender, extraArguments);
			
		case "desc":
			return changeFactionDescription(sender,extraArguments);
			
			
		case "join":
			
			return joinFaction(sender, extraArguments);
			
		case "list":
			
			return listFactions(sender);
			
		case "leave":
			return leaveFaction(sender);
		case "name":
			
			return changeFactionName(sender,extraArguments);
			
			
		case "removepermission":
		case "rp":
			
			return removePermissionHandler(sender, command, extraArguments);
		
		case "owns":
			
			return returnWhoOwns(sender);
			
		case "unclaim":
			
			
			return unclaimLand(sender,command,extraArguments);
			
		case "unclaimall":
			
			return unclaimAllLand(sender);
			
		case "whois":
			
			return showWhoIsReport(sender,extraArguments);

		case "setpermission":
		case "sp":
			
			return false;
		case "info":
			
			return showInfo(sender);
			
		case "show":
			return showFactionInformation(sender, extraArguments);
			
		case "showgroup":
			
			return showGroup(sender, extraArguments);
			
		case "setrelation":
			
			return setRelation(sender,extraArguments); 
		case "setwarp":
			
			return setWarp(sender,extraArguments,player);
			
		case "showrelations":
			
			return showRelations(sender, extraArguments);

		case "grantvisa":
			
			return grantVisa(sender, extraArguments);
		case "revokevisa":
			
			return revokeVisa(sender, extraArguments);
		case "checkvisa":
			
			return checkVisa(sender, extraArguments);
		case "warp":
			
			return teleportToWarp(sender,extraArguments,player);
			
		case "vote":
			//TODO: setup vote command
			break;
			
		case "propose":
			//TODO: setup propose resolution command
			break;

		case "help":
		default:
			sender.sendMessage("--- OpenFactions Commands ---");
			for (int i = 0; i <  Cmd.values().length; i++) {
				sender.sendMessage(Cmd.values()[i].toString());
			}
			return true;
		}
		
		sender.sendMessage(MsgPrefix.ERR + "Unspecified error in executing command.");
		return false;
	}
	
	private long getSecondsLeft(Player player) {
		return ((coolDowns.get(player.getName())/1000) + COOLDOWN_TIME) - (System.currentTimeMillis() / 1000);
	}
	
	private boolean teleportToWarp(CommandSender sender, String[] extraArguments, Player player) {
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction() ) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot use faction warps when you are not in a faction.");
			return false;
		}
		
		if (!Helper.warpExists(extraArguments[1], pi.getPlayerFaction())) {
			sender.sendMessage(MsgPrefix.ERR + "No such warp exists.");
			return false;
		}
		Warp warp = Helper.getWarpByName(extraArguments[1], pi.getPlayerFaction());
		
		if ( !pi.getPlayerGroup().hasPermission(Can.USE_FACTION_WARP) && 
				!warp.getAssociatedGroup().equals(pi.getPlayerGroup())) {
			
			sender.sendMessage(MsgPrefix.ERR + "Your group is not allowed to use faction warps.");
			sender.sendMessage("Groups in your faction that can: " +
					Helper.getGroupsByPermission(pi.getPlayerFaction(), Can.USE_FACTION_WARP));
			return false;
		}
		
		//
		// initiate teleport
		//
		//
		if (coolDowns.containsKey(player.getName())) {
			
			System.out.println(MsgPrefix.DEBUG +"cooldowns contains key");
			
			long secondsLeft = getSecondsLeft(player);
			
			if (secondsLeft > 0) {
				sender.sendMessage(MsgPrefix.ERR +
						"You cannot teleport right now. " + 
				secondsLeft + " seconds left.");
				return true;
			}
			
			coolDowns.put(player.getName(), 
					System.currentTimeMillis());
		} else {
			coolDowns.put(player.getName(), System.currentTimeMillis());
		}
		
		player.teleport(new Location(
				plugin.getServer().getWorld(
						warp.getWorldName()), warp.getWarpX(), 
				warp.getWarpY(), warp.getWarpZ()));
		
		
		return true;
	}

	private boolean showInfo(CommandSender sender) {
		Player player = (Player) sender;
		PlayerInfo pi = new PlayerInfo(player);
		sender.sendMessage(MsgPrefix.INFO + "--- Your Information---");
		if (pi.isPlayerInAFaction()) {
			sender.sendMessage("Your faction: " + pi.getPlayerFaction().getName());
			sender.sendMessage("Faction description: " + pi.getPlayerFaction().getDesc());
			
			sender.sendMessage("All groups in your faction:");
			for (int i = 0 ; i < pi.getPlayerFaction().getGroups().size(); i++) {
				sender.sendMessage(i+1+"." + pi.getPlayerFaction().getGroups().get(i).getName());
			}
			
		} else {
			sender.sendMessage("Your faction: n/a.");
			sender.sendMessage("Faction description: n/a");
			sender.sendMessage("Your group: n/a.");
			
		}
		// they should be in a group...
		if (pi.isPlayerInAGroup()) {
			sender.sendMessage("Your group/division: " + pi.getPlayerGroup().getName());
//			sender.sendMessage("Your permissions: " 
//					+ Helper.formatPermissionsArrayToString(pi.getPlayerGroup().getGroupPermissions()));
		}
		
		
		return true;
	}

	/**
	 * Sets a new faction exclusive warp if possible
	 * @param sender
	 * @param extraArguments
	 * @param player
	 * @return
	 */
	private boolean setWarp(CommandSender sender, String[] extraArguments, Player player) {
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction() || !pi.isPlayerInAGroup()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot set faction warps when you are not in a faction or a faction group.");
			return false;
		}
		
		Faction playerFaction = pi.getPlayerFaction();
		Group playerGroup = pi.getPlayerGroup();
		
		// if the player is not in a group that can set faction warps... 
		if (!Helper.doesGroupHavePermission(Can.SET_FACTION_WARP, playerGroup)) {
			sender.sendMessage(MsgPrefix.ERR + 
					"Your faction does not allow your group ("+
					playerGroup.getName()+") to create new faction warps.");
			
			sender.sendMessage(MsgPrefix.INFO + 
					"Groups your faction allows to set faction warps: ");
			
			ArrayList<Group> groups = Helper.getGroupsByPermission(playerFaction,
					Can.SET_FACTION_WARP);
			// list groups that have this permission/ability
			if (groups != null && groups.size() >= 1) {
				for(int i = 0 ; i < groups.size(); i++) {
					sender.sendMessage("" + i+1 + ". " + groups.get(i).getName());
				}
			} else {
				// rare, highly unlikely, but it is theoretically possible
				// for a faction to remove from all groups that permission
				sender.sendMessage(MsgPrefix.OK + "No one, apparently. Is this supposed to happen?");
			}
			return false;
		}
		
		// by this point the player should have permission
		// first we'll check to see if there is a group specified
		System.out.println("extraArguments[0]:" + extraArguments[0]);
		// we need two arguments in order for this to work
		if ((extraArguments[1] != null //extraArguments[0] ==== warpName
				&& extraArguments[1] != "") && 
				(extraArguments[2] != null && // === player group as string
				extraArguments[2] != "")) {
			
			if ( Helper.doesGroupExist(extraArguments[1], playerFaction) ) {
				
				int x = player.getLocation().getBlockX();
				int y = player.getLocation().getBlockY();
				int z = player.getLocation().getBlockZ();
				
				Warp warp = new Warp(extraArguments[2],x, y, z, 
						plugin.getWorld().getName(), 
						Helper.getGroupFromFactionByName(extraArguments[1], 
								playerFaction));
				
				playerFaction.addWarp(warp);
				
				sender.sendMessage(MsgPrefix.OK + "New warp `" +warp.getWarpName()+ 
						"` created at ["+x+","+y+","+z+"] for group " +
						Helper.getGroupFromFactionByName(extraArguments[1], 
						playerFaction) + "." );
				
				return true;
			} else {
				sender.sendMessage(MsgPrefix.ERR + "Can't assign group specific warp because specified group does not exist.");
				sender.sendMessage(MsgPrefix.INFO + "Try /of info to see a list of groups");
				
				return false;
			}
			
		} 
		//TODO: setup group inspecific warps 
//		else if (extraArguments[0] != null //extraArguments[0] ==== warpName
//				&& extraArguments[0] != "") {
//			
//			// non exclusive groups
//			int x = player.getLocation().getBlockX();
//			int y = player.getLocation().getBlockY();
//			int z = player.getLocation().getBlockZ();
//			
//			Warp warp = new Warp(extraArguments[0],x, y, z, 
//					plugin.getWorld().getName());
//			
//			playerFaction.addWarp(warp);
//			
//			sender.sendMessage(MsgPrefix.OK + "New warp `" +warp.getWarpName()+ 
//					"` created at ["+x+","+y+","+z+"]." );
//			
//			return true;
//		}
		
		
		return false;
	}

	private boolean grantVisa(CommandSender sender, String[] extraArguments) {
		
		UUID visaHolder = null;
		Player player = (Player) sender;
		Date currentDate = new Date();
		Date expirationDate = null;
		String expirationDateString = null;
		String visaClass = "0";
		int visaClassInteger = 0;
		
		//Sender has to specify a player, more specifically, a real one	
		if(extraArguments.length < 2) {
			sender.sendMessage("You must specify a player!");
			return true;
		}
		
		//If the player is not real, exit.		
		try {
			visaHolder = getUuidFromPlayerName(extraArguments[1]);
		} catch(NullPointerException e) {
			sender.sendMessage("You must specify a real player!");
			return true;
		}
		
		if(visaHolder != null) {
			
			if(Helper.isPlayerInAnyFaction(player.getName()) == false) {
				sender.sendMessage("You are not in a faction!");
				return true;
			}
			
			Faction senderFaction = Helper.getPlayerFaction(player.getUniqueId());
			ArrayList<Visa> senderFactionVisaList = senderFaction.getVisas();
			
			//Check to see if the player already has a visa.
			for(int i = 0; i < senderFactionVisaList.size(); i++) {
				Visa v = senderFactionVisaList.get(i);
				if(v.getVisaHolder().equals(visaHolder)) {
					sender.sendMessage("That player already has a visa.");
					return true;
				}
			}				
			switch(extraArguments.length) {
			//If there is only a player name in the arguments, grant a class 0 visa with no expiration date.	
			case(2): 
				Visa visa = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder, visaClassInteger);
				senderFaction.addVisa(visa);
				sender.sendMessage("Granted " + extraArguments[1] + " a Class " + visaClass + " visa for " + senderFaction.getName());
				return true; 		
			case(3):		
				try {
					expirationDateString = extraArguments[2];
					expirationDate = dateFormat.parse(expirationDateString);
				} catch (ParseException e) {e.printStackTrace();/* This is fine, if it's not a date, then it is likely to be a visa class */}
			    //If there is a second argument, but it is an expiration date, then the expiration date will be the 2nd argument, and the class will be zero.
				if (expirationDate instanceof Date) {
					Visa visa2 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder, visaClassInteger);
					senderFaction.addVisa(visa2);
					sender.sendMessage("Granted " + extraArguments[1] + " a Class " + visaClass + " visa for " + senderFaction.getName() + " until " + expirationDateString);
					return true;			
				}
				//If there is a second argument, but it is not an expiration date, then the visa class will be the 2nd argument
				try {
					visaClass = extraArguments[2]; 
					visaClassInteger = Integer.valueOf(visaClass);
					if(visaClassInteger > 5 || visaClassInteger < 0) {
						sender.sendMessage("Visa class must be a number from 0 to 4");
						return true;
					}
				}
				catch (NumberFormatException e) {
					sender.sendMessage("Visa class must be a number");
					return true;
				}
				Visa visa2 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder, visaClassInteger);
				senderFaction.addVisa(visa2);
				sender.sendMessage("Granted " + extraArguments[1] + " a Class " + visaClass + " visa for " + senderFaction.getName());
				return true;
			//If there are three arguments, then they will represent Player, Expiration Date and Class
			case(4): 
				//Class has to be from 0-4
				//Expiration date must be valid
				try {
					expirationDateString = extraArguments[2];
					expirationDate = dateFormat.parse(expirationDateString);
					visaClass = extraArguments[3]; 
					visaClassInteger = Integer.valueOf(visaClass);
					if(visaClassInteger > 5 || visaClassInteger < 0) {
						sender.sendMessage("Visa class must be a number from 0 to 4");
						return true;
					}
				}
				catch (NumberFormatException e) {
					sender.sendMessage("Visa class must be a number from 0 to 4");
					return true;
				} catch (ParseException e) {
					sender.sendMessage("Incorrect format, the correct date format is mm/dd/yyyy");
					return true;
				}
				Visa visa3 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder, visaClassInteger);
				senderFaction.addVisa(visa3);
				sender.sendMessage("Granted " + extraArguments[1] + " a Class " + visaClass + " visa for " + senderFaction.getName() + " until " + expirationDateString);
				return true;
					
			}		
		
		}
		return false;
	}
		
	private boolean revokeVisa(CommandSender sender, String[] extraArguments) {
		
		UUID visaHolder = null;
		Player player = (Player) sender;
		
		//Sender has to specify a player, more specifically, a real one
		
		if(extraArguments.length < 2) {
			sender.sendMessage("You must specify a player!");
			return true;
		}
		
		//If the player is not real, exit.
		
		try {
			visaHolder = getUuidFromPlayerName(extraArguments[1]);
		} catch(NullPointerException e) {
			sender.sendMessage("You must specify a real player!");
			return true;
		}
		
		if(Helper.isPlayerInAnyFaction(player.getName()) == false) {
			sender.sendMessage("You are not in a faction!");
			return true;
		}
		
		Faction senderFaction = Helper.getPlayerFaction(player.getUniqueId());
		ArrayList<Visa> senderFactionVisaList = senderFaction.getVisas();
				
		if(visaHolder != null) {
			
			for(int i = 0; i < senderFactionVisaList.size(); i++) {
				Visa v = senderFactionVisaList.get(i);
				if(v.getVisaHolder().equals(visaHolder)) {
					senderFactionVisaList.remove(i);
					sender.sendMessage("Revoked " + extraArguments[1] + "'s visa");
					return true;
				}
			}
			sender.sendMessage("That player does not have a visa!");
			return true;
			
		}

		return false;
		
	}
	//Utility command to check if a player has a visa 
	private boolean checkVisa(CommandSender sender, String[] extraArguments) {
		UUID visaHolder = null;
		Player player = (Player) sender;
		
		//Sender has to specify a player, more specifically, a real one
		
		if(extraArguments.length < 2) {
			sender.sendMessage("You must specify a player!");
			return true;
		}
		
		//If the player is not real, exit.
		
		try {
			visaHolder = getUuidFromPlayerName(extraArguments[1]);
		} catch(NullPointerException e) {
			sender.sendMessage("You must specify a real player!");
			return true;
		}
		Faction senderFaction = Helper.getPlayerFaction(player.getUniqueId());
		ArrayList<Visa> senderFactionVisaList = senderFaction.getVisas();
				
		if(visaHolder != null) {
			
			for(int i = 0; i < senderFactionVisaList.size(); i++) {
				Visa v = senderFactionVisaList.get(i);
				if(v.getVisaHolder().equals(visaHolder)) {
					sender.sendMessage(extraArguments[1] + " has a Class " + v.getVisaClass() +" visa");
					return true;
				}
			}
			sender.sendMessage("That player does not have a visa!");
			return true;
			
		}
		
		return false;
	}

	private boolean showWhoIsReport(CommandSender sender, String[] extraArguments) {
		UUID uuid = Helper.getUuidFromPlayerName(extraArguments[1]);
        System.out.println("OFDB: whoisReport: arg0:[" + extraArguments[0] + "], arg1:[" + extraArguments[1] + "]");
        if (uuid == null) {
            sender.sendMessage(String.valueOf(extraArguments[1]) + " does not exist.");
            return false;
        }
        
        if ( Helper.isPlayerInAnyFaction(extraArguments[1])) {
        	
	        Faction fac = Helper.getPlayerFaction(uuid);
	        String playerName = Helper.getPlayerNameFromUuid(uuid);
	        
	        sender.sendMessage("--- Who Is " + playerName + "? Report ---");
	        
	        sender.sendMessage(String.valueOf(playerName) + " is a member of the faction called " + fac.getName() + ".");
	        
	        sender.sendMessage("They are in the group called " + Helper.getGroupPlayerIsIn(fac, uuid).getName() + ".");
        } 
        
        ArrayList<Visa> visasThatThePlayerHas = Helper.getVisasOfPlayer(uuid);
        
        if (visasThatThePlayerHas.size() > 0) {
            sender.sendMessage("- Visas -");
            for (Visa visa : visasThatThePlayerHas) {
                String[] fields = Helper.getVisaReport(visa);
                for (int i = 0; i < fields.length; ++i) {
                    sender.sendMessage(fields[i]);
                }
            }
            sender.sendMessage("");
        }
        
        sender.sendMessage(extraArguments[1] + " is not in a faction.");
        return true;
        
		
	}


	private boolean showFactionInformation(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if (extraArguments == null) {
			sender.sendMessage(Helper.getPlayerFaction(player.getUniqueId()).toString());
			return true;
		}
		
		if (!Helper.doesFactionExist(extraArguments[1])) {
			return false;
		}
		//TODO: make more elegant show faction report
		for ( Faction faction1 : CustomNations.factions) {
			//must be bugged
			if (faction1.getName().equalsIgnoreCase(extraArguments[1])) {
				sender.sendMessage(faction1.toString());
			}
		}
		
		return true;
	}


	private boolean showRelations(CommandSender sender, String[] extraArguments) {
		//TODO: don't require all caps for input. Suggestion: use toLower
		
		if(Helper.getFactionByFactionName(extraArguments[1]) == null) {
			sender.sendMessage(extraArguments[1] + " is not a real faction!");
			return false;
		}
		
		Faction fac2 = Helper.getFactionByFactionName(extraArguments[1]);
		sender.sendMessage(Faction.getRelationshipString(fac2));
		
		return true;
	}
  
  	
	private boolean assignToGroup(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if(!isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		
		if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.ASSIGN_GROUPS, group )== false ) {
			sender.sendMessage("You aren't allowed to assign people to a group.");
			return false;
		} 
		
		Group groupToEdit = Helper.getGroupFromFactionByName(
				extraArguments[1], fac);
		
		UUID uuid = Commands.getUuidFromPlayerName(extraArguments[2]);
		
		if (uuid == null) {
			sender.sendMessage("Specified player does not exist.");
			return false;
		}
		
		
		if (groupToEdit.getMembers().contains(uuid)) {
			
			sender.sendMessage("Player is already in group called " 
					+ extraArguments[1] + ".");
			return false;
		}
		group.removeMember(uuid);
		groupToEdit.addMember(uuid);
		
		fac.setGroupAtIndex(fac.getGroups().lastIndexOf(group), group);
		fac.setGroupAtIndex(fac.getGroups().indexOf(groupToEdit), groupToEdit);
		
		return true;
	}


	private boolean changeFactionDescription(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());

		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.CHANGE_FACTION_DESC, group )== false ) {
			sender.sendMessage("You aren't allowed to change the faction description.");
			return false;
		}  else {
			
			String newDescription = "";
			
			for ( int i = 0 ; i < 0; i ++) {
				System.out.println(extraArguments[i]);
				newDescription +=" "+ extraArguments[i];
			}
			
			fac.setDesc(newDescription);
			
			sender.sendMessage(MsgPrefix.OK + "Faction description is now: ");
			sender.sendMessage(newDescription);
			
			return true;
		}
		
		
	}


	private boolean changeFactionName(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if (player==null ) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot change faction description because you aren't in one.");
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());

		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.CHANGE_FACTION_NAME, group )== false ) {
			sender.sendMessage("You aren't allowed to change the faction name.");
			return false;
		} 
		
		if (extraArguments[1] != null) {
			sender.sendMessage(MsgPrefix.OK + 
					"Faction name has been changed to `" 
					+ extraArguments[1] + ".`");
			fac.setName(extraArguments[1]);
		} else {
			return false;
		}
		
		return true;
	}


	private boolean removePermissionHandler(CommandSender sender, Command command, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if(!isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		
		if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( Helper.doesGroupHavePermission(Can.EDIT_GROUPS, group )== false ) {
			sender.sendMessage("You aren't allowed to edit this particular group.");
			return false;
		} 
		
		Group groupToEdit = Helper.getGroupFromFactionByName(extraArguments[1], fac);
		
		if (!Helper.doesStringMatchAValidPermission(extraArguments[2])) {
			sender.sendMessage("Invalid permission: " +extraArguments[2] + ". Try /of help");
			return false;
		}
		//not sure if it is totally necessary to do this
		//fac.removeGroup(groupToEdit);
		
		if (!groupToEdit.hasPermission(Can.valueOf(extraArguments[2]))) {
			sender.sendMessage("This permission is not in the list of permissions for this group already!");
			return false;
		}
		
		groupToEdit.removePermission(Can.valueOf(extraArguments[2]));
		
		//fac.addGroup(groupToEdit);
		
		fac.setGroupAtIndex(fac.getGroups().indexOf(groupToEdit), groupToEdit);//( fac.getGroups().get(fac.getGroups().indexOf(groupToEdit)).removePermission(Can.valueOf(extraArguments[2]))  );
		
		Faction.serialize(fac, fac.getAutoFileName());
		
		return true;
	}


	private boolean addPermissionHandler(CommandSender sender, Command command, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if(!isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		
		if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Helper.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( !Helper.doesGroupHavePermission(Can.EDIT_GROUPS, group) ) {
			sender.sendMessage("You aren't allowed to edit this particular group.");
			return false;
		} 
		
		Group groupToEdit = Helper.getGroupFromFactionByName(extraArguments[1], fac);
		
		if (!Helper.doesStringMatchAValidPermission(extraArguments[2])) {
			sender.sendMessage("Invalid permission: " +extraArguments[2] + ". Try /of help");
			return false;
		}
		
		if (groupToEdit.hasPermission(Can.valueOf(extraArguments[2]))) {
			sender.sendMessage("This permission has already been added.");
			return false;
		}
		
		//not sure if it is totally necessary to do this
		
		
		groupToEdit.addPermission(Can.valueOf(extraArguments[2]));
		
		fac.setGroupAtIndex(fac.getGroups().indexOf(groupToEdit), groupToEdit);
		
		Faction.serialize(fac, fac.getAutoFileName());
		
		return true;
	}


	private boolean isValid(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player == null) { 
			sender.sendMessage("You cannot issue this command as console.");
			return false; 
		}
		
		if (extraArguments.length < 3) {
			sender.sendMessage("Insufficient number of arguments.");
			return false;
		}
		
		if (!Helper.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage("You are not in a faction.");
			return false;
		} 
		return true;
	}


	private boolean showGroup(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player == null) { 
			sender.sendMessage("You cannot issue this command as console.");
			return false; 
		}
		
//		if (extraArguments.length == 2) {
//			sender.sendMessage("Insufficient number of arguments.");
//			return false;
//		}
//		
//		
//		if (extraArguments.length < 1) {
//			sender.sendMessage("Insufficient number of arguments.");
//			return false;
//		}
		
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			if (Helper.doesGroupExist( extraArguments[1], fac) == false) {
				sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
				return false;
			}
			
			sender.sendMessage("--- Group Information ---");
			sender.sendMessage( Helper.getGroupFromFactionByName(extraArguments[1], fac).toString() );
		}
		
		return true;
	}


	private boolean createGroup(CommandSender sender, String[] extraArguments) {
		// cast
		Player player = (Player) sender;
		
		if (player == null) { 
			sender.sendMessage("You cannot issue this command as console.");
			return false; 
		}
		
		if (extraArguments.length < 2) {
			sender.sendMessage("Insufficient number of arguments.");
			return false;
		}
		
		
		
		//determine if player is in a faction
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			if (Helper.doesGroupExist( extraArguments[1], fac)) {
				sender.sendMessage("This group of " + extraArguments[1] + " already exists!" );
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
				
				sender.sendMessage("New group ["+extraArguments[1]+"] created.");
				Faction.serialize(fac, fac.getAutoFileName());
			}
			
		} else {
			return false;
		}
		
		
		return true;
	}

	private boolean listFactions(CommandSender sender) {
		sender.sendMessage("List of factions - Output");
		
		for ( int i = 0 ; i < CustomNations.factions.size(); i++ ) {
			sender.sendMessage(CustomNations.factions.get(i).toString());
		}
		return true;
	}

	private boolean setRelation(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if (player == null) {
			sender.sendMessage("The console may not join a faction.");
			return false;
		}
		
		if(Helper.getPlayerFaction(player.getUniqueId()) == null) {
			sender.sendMessage("You are not in a faction!");
			return false; 
		}
		
		Faction faction1 = Helper.getPlayerFaction(player.getUniqueId()); 
		if(extraArguments[1].equalsIgnoreCase(faction1.getName()) || Helper.getFactionByFactionName(faction1.getName()) == null) {
			sender.sendMessage("That faction name is invalid!");
			return false;
		}
		
		String faction1Name = faction1.getName(); 
		faction1.setRelationshipByFactionName(faction1Name, extraArguments[1], RelationshipType.valueOf(extraArguments[2]));
		Bukkit.broadcastMessage(faction1Name + "declared that they are now an " + extraArguments[2].toUpperCase() + " to " + extraArguments[1]);
		
		return true;
	}


	private boolean joinFaction(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		if (player == null) {
			sender.sendMessage("The console may not join a faction.");
			return false;
		}
		
		//we'll assume that we don't need
		//an invitation
		if (Helper.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage("You cannot join a faction as you are already in one!");
			return false;
		} else {
			for (Faction fac : CustomNations.factions) {
				if (fac.getName().equalsIgnoreCase(extraArguments[1])) {
					fac.addMember(player.getUniqueId());
					Group def = fac.getDefaultGroup();
					
					fac.removeGroup(def);
					
					def.addMember(player.getUniqueId());
					
					fac.addGroup(def);
					
					sender.sendMessage("You have joined " + fac.getName()+".");
				}
				
			}
			
		}
		return true;
	}


	private boolean createFaction(CommandSender sender, String[] extraArguments) {
		Player player = (Player) sender;
		
		//null check (check if CommandSender is the console)
		if (player == null ) {
			sender.sendMessage("Faction creation as the console is not allowed.");
			return false;
		} else if(Helper.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage("You are already in a faction."
					+ " You must first leave your faction "
					+ "in order to make a new one.");
			return false;
		} else if (extraArguments.length > 2) {
			sender.sendMessage("You may not have spaces in your faction name.");
			return false;
		} else if (Helper.doesFactionExist(extraArguments[1])) {
			sender.sendMessage("You may not create a faction with name " 
					+ extraArguments[1] + " because it already exists.");
			return false;
		}
		
		//get player's unique id
		Faction faction = new Faction(extraArguments[1], player.getUniqueId());
		
		CustomNations.factions.add(faction);
		sender.sendMessage("You have created " + faction.getName() + ".");
		
		Faction.serialize(faction, faction.getAutoFileName());
		
		return true;
	}


	private boolean leaveFaction(CommandSender sender) {
		Player pl = (Player) sender;
		UUID plUuid = pl.getUniqueId();
		Faction fac = Helper.getPlayerFaction(plUuid);
		
		if (fac != null) {
			fac.removeMember(plUuid);
			pl.sendMessage("You have left " + fac.getName() + ".");
			//if you are the last member of the faction
			//delete the faction
			if ( fac.getMembers().size() < 1 ) {
				pl.sendMessage("You have disbanded "+fac.getName()+".");
				CustomNations.factions.remove(fac);
				CustomNations.deleteFactionSave(fac.getAutoFileName());
				return true;
			}
			
		} else {
			sender.sendMessage("You are not in a real faction!");
		}
		return false;
	}


	private boolean returnWhoOwns(CommandSender sender) {
		
		Player player ;
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		Chunk chunk = player.getLocation().getChunk();
		
		if ( Helper.isSpecifiedChunkInsideAnyFaction( chunk)) {
			
			ArrayList<Faction> facs = Helper.returnFactionObjectsWhereChunkIsFoundIn(chunk);
			
			sender.sendMessage("--- Land claim ownership ---");
			for(Faction fac : facs) {
				
				LandClaim lc = Helper.returnLandClaimContainingSpecifiedChunk(chunk);
				
				sender.sendMessage("Claimed by "+ fac.getName() + (  lc.getClaimDescriptor().isEmpty() ? ". " +lc.getClaimDescriptor() : "." ) );
			}
			
			return true;
			
		} else {
			sender.sendMessage("This land is not claimed by anyone.");
		}
		
		return false;
	}


	private boolean unclaimAllLand(CommandSender sender) {
		
		//TODO: account for faction member rank
		Player player ;
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		//if player is in a faction
		if ( Helper.isPlayerInAnyFaction(player.getDisplayName()) ) {
			Faction fac = Helper.getPlayerFaction(player.getUniqueId());
			
			for (LandClaim lc : fac.getClaims()) {
				fac.removeClaim(lc);
			}
			sender.sendMessage("You have unclaimed this all land claims.");
			Faction.serialize(fac, fac.getAutoFileName());
			return true;
		}
		
		return false;
	}


	private boolean unclaimLand(CommandSender sender, Command command, String[] extraArguments) {
		
		//TODO: account for faction member rank
		Player player ;
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		//get players faction
		Faction fac = Helper.getPlayerFaction(player.getUniqueId());
		if (fac == null) {
			sender.sendMessage("You can't do this because you are not in a faction.");
			return false;
		}
		
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			LandClaim lc = Helper.returnLandClaimContainingSpecifiedChunk(chunk);
			
			if(lc == null) {
				return false;
			}
			
			// we don't risk unclaiming land from another faction
			for (LandClaim landClaim : fac.getClaims()) {
				//because fac.getClaims() is just claims for the faction
				//that the current player is in as a member
				if ( landClaim.equals(lc) ) {
					fac.removeClaim(lc);
					sender.sendMessage("You have unclaimed this land claim.");
					Faction.serialize(fac, fac.getAutoFileName());
					return true;
				}
			}
			
			if ( Helper.isSpecifiedLandClaimInsideAnyFaction(lc)) {
				//TODO: account for diplomacy
				sender.sendMessage("This territory is owned by a different faction.");
				return false;
			} else {
				fac.addClaim(new LandClaim());
			}
			
		}
		
		return false;
	}


	private boolean claimLand(CommandSender sender, Command command, 
			String[] extraArguments) {
		Player player ;
		
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		Faction fac = pi.getPlayerFaction();
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage("Land claim failed! You are not in a faction.");
			return false;
		}
		
		//if we are to claim one at a time
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			if(!Helper.doesGroupHavePermission(Can.CLAIM, pi.getPlayerGroup())) {
				sender.sendMessage(MsgPrefix.ERR + "You do not have permission to claim land in your faction.");
				sender.sendMessage(MsgPrefix.OK + "Groups in your faction that can claim: " + Helper.getGroupsByPermission(fac, Can.CLAIM));
				return false;
			}
			
			if ( Helper.isSpecifiedChunkInsideAnyFaction(chunk)  ) {
				//TODO: account for diplomacy
				//TODO: account for contest claiming
				sender.sendMessage("This territory is already claimed.");
				return false;
			} else {
				fac.addClaim(new LandClaim(chunk));
				sender.sendMessage("You have successfully claimed this chunk.");
				Faction.serialize(fac, fac.getAutoFileName());
			}
			
		}
		
		
		return true;
	}


	public static UUID getUuidFromPlayerName(String name) {
		Player userPlayer = Bukkit.getPlayer(name);
		return userPlayer.getUniqueId();
	}
	
	public static String getPlayerNameFromUuid(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		return player.getDisplayName();
	}
	
	
	

}
