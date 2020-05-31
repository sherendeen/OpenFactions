//Copyright 2018-2020
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

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.entity.Player;

import openFactions.CustomNations;

enum Cmd {
	ADDPERMISSION,
	AP,/**ADDPERMISSION ALIAS*/
	CLAIM,
	CREATE,
	CREATEGROUP,
	CG,/**alias for CreateGroup*/
	JOIN,
	LIST,
	LEAVE,
	OWNS,
	REMOVEPERMISSION,
	RP,/**RemovePermission Alias*/
	SETGROUP,
	SETPERMISSION,
	SP,/** alias for SetPermission */
	SETRELATION,
	SHOW,
	SHOWGROUP,
	SHOWRELATIONS,
	UNCLAIM,
	UNCLAIMALL,
	WHOIS
}

public class Commands implements CommandExecutor{
	
	CustomNations plugin;
	public Commands(CustomNations plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] extraArguments) {
		Player player ;
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		
		
		if(command.getName().equalsIgnoreCase("of") && extraArguments.length > 0) {
			
			switch (extraArguments[0].toLowerCase()) {
			case "addpermission":
			case "ap":
				
				return addPermissionHandler(sender, command, extraArguments);
				
			case "claim":
				//Keeping things neat by putting the bulk of the code outside this case block
				return claimLand(sender,command,extraArguments);
				
			case "create":
				
				return createFaction(sender, extraArguments);
				
			case "creategroup":
			case "cg":
				
				return createGroup(sender, extraArguments);
				
			case "join":
				
				return joinFaction(sender, extraArguments);
				
			case "list":
				
				return listFactions(sender);
				
			case "leave":
				return leaveFaction(sender);
			case "removeposition":
			case "rp":
				
				return removePositionHandler(sender, command, extraArguments);
			
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
				
			case "show":
				return showFactionInformation(sender, extraArguments);
			case "showgroup":
				
				return showGroup(sender, extraArguments);
				
			case "setrelation":
				
				return setRelation(sender,extraArguments); 
				
			case "showrelations":
				
				return showRelations(sender, extraArguments);
			case "help":
			default:
				sender.sendMessage("--- OpenFactions Commands ---");
				for (int i = 0; i <  Cmd.values().length; i++) {
					sender.sendMessage(Cmd.values()[i].toString());
				}
				return true;
			}
			
		} else {
			return false;
		}
	}
	
	private boolean removePositionHandler(CommandSender sender, Command command, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if(!isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
		
		if (Faction.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Faction.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( !Group.doesGroupHavePermission(Can.EDIT_GROUPS, group) ) {
			sender.sendMessage("You aren't allowed to edit this particular group.");
			return false;
		} 
		
		Group groupToEdit = Faction.getGroupFromFactionByName(extraArguments[1], fac);
		
		if (!Group.doesStringMatchAValidPermission(extraArguments[2])) {
			sender.sendMessage("Invalid permission: " +extraArguments[2] + ". Try /of help");
			return false;
		}
		//not sure if it is totally necessary to do this
		fac.removeGroup(groupToEdit);
		
		if (!groupToEdit.hasPermission(Can.valueOf(extraArguments[2]))) {
			sender.sendMessage("This permission is not in the list of permissions for this group already!");
			return false;
		}
		
		groupToEdit.removePermission(Can.valueOf(extraArguments[2]));
		
		fac.addGroup(groupToEdit);
		
		Faction.serialize(fac, fac.getAutoFileName());
		
		return true;
	}


	private boolean addPermissionHandler(CommandSender sender, Command command, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if(!isValid(sender, extraArguments)) {
			return false;
		}
		
		Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
		
		if (Faction.doesGroupExist( extraArguments[1], fac) == false) {
			sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
			return false;
		}
		
		Group group = Faction.getGroupPlayerIsIn(fac, player.getUniqueId());
		
		if ( !Group.doesGroupHavePermission(Can.EDIT_GROUPS, group) ) {
			sender.sendMessage("You aren't allowed to edit this particular group.");
			return false;
		} 
		
		Group groupToEdit = Faction.getGroupFromFactionByName(extraArguments[1], fac);
		
		if (!Group.doesStringMatchAValidPermission(extraArguments[2])) {
			sender.sendMessage("Invalid permission: " +extraArguments[2] + ". Try /of help");
			return false;
		}
		
		if (groupToEdit.hasPermission(Can.valueOf(extraArguments[2]))) {
			sender.sendMessage("This permission has already been added.");
			return false;
		}
		
		//not sure if it is totally necessary to do this
		fac.removeGroup(groupToEdit);
		
		groupToEdit.addPermission(Can.valueOf(extraArguments[2]));
		
		fac.addGroup(groupToEdit);
		
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
		
		if (!Faction.isPlayerInAnyFaction(player.getDisplayName())) {
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
		
		if (extraArguments.length == 2) {
			sender.sendMessage("Insufficient number of arguments.");
			return false;
		}
		
		
		if (extraArguments.length < 2) {
			sender.sendMessage("Insufficient number of arguments.");
			return false;
		}
		
		if ( Faction.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
			
			if (Faction.doesGroupExist( extraArguments[1], fac) == false) {
				sender.sendMessage("This group of " + extraArguments[1] + " does NOT exist!" );
				return false;
			}
			
			sender.sendMessage("--- Group Information ---");
			sender.sendMessage( Faction.getGroupFromFactionByName(extraArguments[1], fac).toString() );
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
		
		if (extraArguments.length == 2) {
			sender.sendMessage("Insufficient number of arguments.");
			return false;
		}
		
		
		
		//determine if player is in a faction
		if ( Faction.isPlayerInAnyFaction(player.getDisplayName()) ) {
			
			Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
			
			if (Faction.doesGroupExist( extraArguments[1], fac)) {
				sender.sendMessage("This group of " + extraArguments[3] + " already exists!" );
				return false;
			}
			
			Group group = Faction.getGroupPlayerIsIn(fac, player.getUniqueId());
			
			if (Group.doesGroupHavePermission(Can.MAKE_OR_REMOVE_GROUPS, group)) {
				//inherit from the group you are in but have a different name
				Group newGroup = group;
				newGroup.setName(extraArguments[3]);
				// we don't want to inherit the members of the group
				newGroup = Group.removeAllMembersFromGroup(newGroup);
				fac.addGroup(newGroup);
				
				sender.sendMessage("New group ["+extraArguments[1]+"] created.");
				Faction.serialize(fac, fac.getAutoFileName());
			}
			
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


	private boolean showWhoIsReport(CommandSender sender, String[] extraArguments) {
		UUID uuid = getUuidFromPlayerName(extraArguments[1]);
		
		if (uuid != null) {
			
			if(Faction.doesFactionExist(extraArguments[1])) {
				Faction fac1 = Faction.returnFactionThatPlayerIsIn(uuid);
				//TODO: improve output
				sender.sendMessage(fac1.toString());
				return true;
			} else {
				sender.sendMessage(extraArguments[1] + " is not in a faction.");
				return true;
			}
			
			
		} else {
			sender.sendMessage(extraArguments[1] + " does not exist.");
		}
		return false;
	}


	private boolean showFactionInformation(CommandSender sender, String[] extraArguments) {
		if (!Faction.doesFactionExist(extraArguments[1])) {
			return false;
		}
		
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
		
		if(Faction.getFactionByFactionName(extraArguments[1]) == null) {
			sender.sendMessage(extraArguments[1] + " is not a real faction!");
			return false;
		}
		
		Faction fac2 = Faction.getFactionByFactionName(extraArguments[1]);
		sender.sendMessage(Faction.getRelationshipString(fac2));
		
		return true;
	}


	private boolean setRelation(CommandSender sender, String[] extraArguments) {
		
		Player player = (Player) sender;
		
		if (player == null) {
			sender.sendMessage("The console may not join a faction.");
			return false;
		}
		
		if(Faction.returnFactionThatPlayerIsIn(player.getUniqueId()) == null) {
			sender.sendMessage("You are not in a faction!");
			return false; 
		}
		
		Faction faction1 = Faction.returnFactionThatPlayerIsIn(player.getUniqueId()); 
		if(extraArguments[1].equalsIgnoreCase(faction1.getName()) || Faction.getFactionByFactionName(faction1.getName()) == null) {
			sender.sendMessage("That faction name is invalid!");
			return false;
		}
		
		String faction1Name = faction1.getName(); 
		faction1.setRelationshipByFactionName(faction1Name, extraArguments[1], relationshipTypes.valueOf(extraArguments[2]));
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
		if (Faction.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage("You cannot join a faction as you are already in one!");
			return false;
		} else {
			for (Faction fac : CustomNations.factions) {
				if (fac.getName().equalsIgnoreCase(extraArguments[1])) {
					fac.addMember(player.getUniqueId());
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
		} else if(Faction.isPlayerInAnyFaction(player.getDisplayName())) {
			sender.sendMessage("You are already in a faction."
					+ " You must first leave your faction "
					+ "in order to make a new one.");
			return false;
		} else if (extraArguments.length > 2) {
			sender.sendMessage("You may not have spaces in your faction name.");
			return false;
		} else if (Faction.doesFactionExist(extraArguments[1])) {
			sender.sendMessage("You may not create a faction with name " 
					+ extraArguments[1] + " because it already exists.");
			return false;
		}
		
		//get player's unique id
		Faction faction = new Faction(extraArguments[1], player.getUniqueId());
		
		CustomNations.factions.add(faction);
		Faction.serialize(faction, faction.getAutoFileName());
		
		return true;
	}


	private boolean leaveFaction(CommandSender sender) {
		Player pl = (Player) sender;
		UUID plUuid = pl.getUniqueId();
		Faction fac = Faction.returnFactionThatPlayerIsIn(plUuid);
		
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
		
		if ( LandClaim.isSpecifiedChunkInsideAnyFaction( chunk)) {
			
			ArrayList<Faction> facs = LandClaim.returnFactionObjectsWhereChunkIsFoundIn(chunk);
			
			sender.sendMessage("--- Land claim ownership ---");
			for(Faction fac : facs) {
				
				LandClaim lc = LandClaim.returnLandClaimContainingSpecifiedChunk(chunk);
				
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
		if ( Faction.isPlayerInAnyFaction(player.getDisplayName()) ) {
			Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
			
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
		Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
		if (fac == null) {
			sender.sendMessage("You can't do this because you are not in a faction.");
			return false;
		}
		
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			LandClaim lc = LandClaim.returnLandClaimContainingSpecifiedChunk(chunk);
			
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
			
			if ( LandClaim.isSpecifiedLandClaimInsideAnyFaction(lc)) {
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
		
		//TODO: account for faction member rank
		
		if ( !( sender instanceof Player )) {
			player = null;
		} else {
			player = (Player) sender;
		}
		
		Faction fac = Faction.returnFactionThatPlayerIsIn(player.getUniqueId());
		if (fac == null) {
			sender.sendMessage("Land claim failed! You are not in a faction.");
			return false;
		}
		
		//if we are to claim one at a time
		if ( extraArguments.length == 1 ) {
			
			//get chunk player is in
			Chunk chunk = player.getLocation().getChunk();
			if ( LandClaim.isSpecifiedChunkInsideAnyFaction(chunk)  ) {
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
