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
	CLAIM,
	CREATE,
	JOIN,
	LIST,
	LEAVE,
	OWNS,
	SHOW,
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
				
			case "claim":
				//Keeping things neat by putting the bulk of the code outside this case block
				return claimLand(sender,command,extraArguments);
				
			case "create":
				//player.sendMessage(ChatColor.RED + "Attempting to create faction " + extraArguments[1]);
				System.out.println("Attempting to create faction");
				
				//get player's unique id
				Faction faction = new Faction(extraArguments[1], player.getUniqueId());
				
				CustomNations.factions.add(faction);
				Faction.serialize(faction, faction.getAutoFileName());
				
				//"faction_" + 
				//CustomNations.factions.lastIndexOf(faction) + 
				//"_" + faction.getSerialUUID()+".fbin"
				
				return true;
				
			case "join":
				
				//we'll assume that we don't need
				//an invitation
				if (Faction.isPlayerInAnyFaction(sender.getName())) {
					sender.sendMessage("You cannot join a faction as you are already in one!");
				} else {
					for (Faction fac : CustomNations.factions) {
						if (fac.getName().equalsIgnoreCase(extraArguments[1])) {
							fac.addMember(player.getUniqueId());
							sender.sendMessage("You have joined " + fac.getName()+".");
							return true;
						}
						
					}
					
				}
				
				return false;
				
			case "list":
				//TODO: list faction names and number of members
				//instead of showing entire toString() for each faction
				sender.sendMessage("List of factions - Output");
				
				for ( int i = 0 ; i < CustomNations.factions.size(); i++ ) {
					sender.sendMessage(CustomNations.factions.get(i).toString());
				}
				return true;
				
			case "leave":
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
					return false;
				}
			case "owns":
				
				return returnWhoOwns(sender);
				
			case "unclaim":
				
				return unclaimLand(sender,command,extraArguments);
				
			case "unclaimall":
				
				return unclaimAllLand(sender);
				
			case "whois":
				
				UUID uuid = getUuidFromPlayerName(extraArguments[1]);
				if (uuid != null) {
					
					Faction fac1 = Faction.returnFactionThatPlayerIsIn(uuid);
					
					if (fac1 != null) {
						sender.sendMessage( fac1.toString() ); 
						return true;
					} else {
						sender.sendMessage(extraArguments[1] 
								+ " is either not in a faction or is not a real player.");
						return false;
					}
				}

			case "show":
				
				for ( Faction faction1 : CustomNations.factions) {
					//must be bugged
					if (faction1.getName().equalsIgnoreCase(extraArguments[1])) {
						sender.sendMessage(faction1.toString());
						return true;
					}
				}
				return false;
			case "setrelation":
				//TODO: create method(s) from this
				if(Faction.returnFactionThatPlayerIsIn(player.getUniqueId()) == null) {
					sender.sendMessage("You are not in a faction!");
					return false; 
				}
				Faction faction1 = Faction.returnFactionThatPlayerIsIn(player.getUniqueId()); 
				if(extraArguments[1].equalsIgnoreCase(faction1.getName()) || Faction.getFactionByFactionName(faction1.getName()) == null) {
					sender.sendMessage("That faction name is invalid!");
					return true;
				}
				String faction1Name = faction1.getName(); 
				faction1.setRelationshipByFactionName(faction1Name, extraArguments[1], relationshipTypes.valueOf(extraArguments[2]));
				Bukkit.broadcastMessage(faction1Name + "declared that they are now an " + extraArguments[2].toUpperCase() + " to " + extraArguments[1]);
				
				return true; 
			case "showrelations":
				//TODO: create relevant method for show relations
          //TODO: don't require all caps for input. Suggestion: use toLower
				if(Faction.getFactionByFactionName(extraArguments[1]) == null) {
					sender.sendMessage(extraArguments[1] + " is not a real faction!");
					return false;
				}
				Faction fac2 = Faction.getFactionByFactionName(extraArguments[1]);
				sender.sendMessage(Faction.getRelationshipString(fac2));
				return true;
          
			default:
				return true;
			}
			
		} else {
			return false;
		}
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
