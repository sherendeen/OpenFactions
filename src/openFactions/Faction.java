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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.io.File;

/**
 * 
 * @author Seth G. R. Herendeen [inivican]
 *
 */
public class Faction implements Serializable {

	private String name;
	private String dateCreated;
//	private ArrayList<String> members = new ArrayList<String>();
	
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	private ArrayList<LandClaim> claimList = new ArrayList<LandClaim>();
	private UUID serialUUID;

	public Faction(String name, Date dateCreated, ArrayList<UUID> members, ArrayList<LandClaim> claimList) {

		this.name = name;
		this.dateCreated = dateCreated.toString();
		this.members = members;
		this.claimList = claimList;

		this.serialUUID = UUID.randomUUID();

	}

	public Faction(String name, String dateCreatedStr, ArrayList<UUID> members, ArrayList<LandClaim> claims) {
		this.name = name;
		this.dateCreated = dateCreatedStr;
		this.members = members;
		this.claimList = claims;

		this.serialUUID = UUID.randomUUID();
	}

	/**
	 * Constructor for faction that doesn't assume too much of the user
	 * 
	 * @param name                       the name of the faction
	 * @param personWhoCreatedTheFaction self commenting, self explanatory
	 */
	public Faction(String name, UUID personWhoCreatedTheFaction) {
		this.dateCreated = new Date().toString();
		this.name = name;
		this.members.add(personWhoCreatedTheFaction);

		this.serialUUID = UUID.randomUUID();
	}

	/**
	 * Returns the readonly UUID. Must stay unique.
	 * 
	 * @return uuid of the faction
	 */
	public UUID getSerialUUID() {
		return this.serialUUID;
	}

	/**
	 * Returns the name of the faction
	 * 
	 * @author ZettaX
	 * @return String
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Nation [name=" + name + ", dateCreated=" + dateCreated + ", members=" + returnListOfNames(members) + ", claimList="
				+ claimList + ", serialUUID=" + serialUUID + "]";
	}

	/**
	 * Returns the date the faction was created
	 * 
	 * @author ZettaX
	 * @return String
	 */
	public String getCreationDate() {
		return this.dateCreated;
	}

	/**
	 * Returns the list of members currently in the faction
	 * 
	 * @author ZettaX
	 * @return String (ArrayList)
	 */
	public ArrayList<UUID> getMembers() {
		return members;
	}

	/**
	 * Returns the faction's list of claims
	 * 
	 * @author ZettaX
	 * @return LandClaim (ArrayList)
	 */
	public ArrayList<LandClaim> getClaims() {
		return claimList;
	}
	
	public void setClaims(ArrayList<LandClaim> claims) {
		this.claimList = claims;
	}
	
	/**
	 * Adds a new claim to the list of claims
	 * Doesn't check validity of claim
	 * @param claim faction claim in question
	 */
	public void addClaim(LandClaim claim) {
		this.claimList.add(claim);
	}
	
	public void removeClaim(LandClaim claim) {
		this.claimList.remove(claim);
	}
	
	public void addMember(UUID uuid) {
		this.members.add(uuid);
	}
	
	public void removeMember(UUID uuid) {
		this.members.remove(uuid);
	}

	public static Faction makeTestFaction() {

//		Faction fac = new Faction("Testificate", "inivican");
//		fac.members.add("ZettaX");
//		fac.members.add("Gagger");
		
		Faction fac = new Faction("Testificate", UUID.fromString("622440c0-2638-4c18-9b41-e99adc00683d"));
		fac.members.add(UUID.fromString("5b6692b5-0b67-49d6-bfcc-3bf4a04a2c72"));
	//	fac.members.add(UUID.fromString("cff474e0-cdad-4076-bafa-d1daa0d0d8d5"));
		
		return fac;
	}
	
	public String getAutoFileName() {
		return "faction_" + getSerialUUID() + "_.fbin";
	}

	/**
	 * Saves the FACTION INSTANCE to a binary file
	 * 
	 * @param faction  instance of the faction (i.e the faction you want to save)
	 * @param fileName the name of the file
	 */
	public static void serialize(Faction faction, String fileName) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName);

			ObjectOutputStream objOuputStream = new ObjectOutputStream(fileOutputStream);

			objOuputStream.writeObject(faction);

			fileOutputStream.close();
			objOuputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Serialized filename: [" + fileName + "],\nfaction name: ["+faction.getName()+"],\n UUID: ["+faction.getSerialUUID()+"]");
		}
	}

	public static Faction deserialize(String fileName) {
		Faction faction = null;

		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			// attempt to read and deserialize
			faction = (Faction) objectInputStream.readObject();

			objectInputStream.close();
			fileInputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return faction;
	}
	/**
	 * finds a uuid in a haystack of factions
	 * @param factionsList 
	 * @param uuid unique identifier of a faction
	 * @return
	 */
	public static int getFactionFromUUID(ArrayList<Faction> factionsList, UUID uuid) {
		for (int i = 0; i < factionsList.size(); i++) {
			if (factionsList.get(i).getSerialUUID() == uuid) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the faction object that contains a specified member by UUID
	 * @param playerUUID
	 * @return faction object containing member uuid
	 */
	public static Faction returnFactionThatPlayerIsIn(UUID playerUUID) {
		
		for ( Faction fac : CustomNations.factions ) {
			for (UUID member : fac.getMembers()) {
				if (member.equals(playerUUID)) {
					return fac;
				}
			}
		}
		
		return null;
	}
	
	public static ArrayList<String> returnListOfNames(ArrayList<UUID> uuids) {
		ArrayList<String> names = new ArrayList<String>();
		
		for ( int i = 0 ; i < uuids.size(); i++) {
			names.add( Commands.getPlayerNameFromUuid(uuids.get(i)));
		}
		
		
		
		return names;
	}
	
	/**
	 * 
	 * @param player name display string
	 * @return returns true if the player is in a faction. Returns false if not.
	 */
	public static boolean isPlayerInAnyFaction(String name) {
		
		UUID uuid = Commands.getUuidFromPlayerName(name);
		
		boolean result = false;
		
		for (Faction fac : CustomNations.factions) {
			result = isPlayerInSpecifiedFaction(uuid, fac);
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param uuid player UUID
	 * @param faction faction object
	 * @return whether or not the specified uuid is in the faction
	 */
	public static boolean isPlayerInSpecifiedFaction(UUID uuid, Faction faction) {
		if (faction != null) {
			for (int i = 0; i < faction.getMembers().size(); i++ ) {
				if (faction.getMembers().get(i).equals(uuid)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	

}
