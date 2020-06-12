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
import java.util.HashMap;
import java.util.UUID;

/**
 * 
 * @author Seth G. R. Herendeen [inivican]
 *
 */

enum relationshipTypes {
	NEUTRAL, 
	ALLY,
	ENEMY,
	TRUCE,
	LORD,
	VASSAL	
}

public class Faction implements Serializable {

	private String name;
	
	private String dateCreated;
	private ArrayList<Visa> visas = new ArrayList<Visa>();
	private String desc;
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	private HashMap<String, relationshipTypes> relationships = new HashMap<String, relationshipTypes>(); 
	/** 
	 * List of land claims made by this faction.
	 * Encapsulates chunk coordinates, string descriptors, and 
	 * what group might have exclusive editing access
	 */
	private ArrayList<LandClaim> claimList = new ArrayList<LandClaim>();
	
	//TODO: reconcile whether we actually need this for anything other than naming files
	private UUID serialUUID;
	/**
	 * The group that new members to the faction
	 * are put inside of automatically
	 */
	private Group defaultGroup;
	private ArrayList<Group> groups = new ArrayList<Group>(); 

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
		
		Group adminGroup = Group.createAdminGroup();
		Group commons = Group.createCommonGroup();
		System.out.println("Adding user to new faction into admin group");
		adminGroup.addMember(personWhoCreatedTheFaction);
		this.groups.add(adminGroup);
		this.groups.add(commons);
		this.defaultGroup = commons;
		
		this.serialUUID = UUID.randomUUID();
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}
	
	public void addGroup(Group group) {
		this.groups.add(group);
	}
	
	public void removeGroup(Group group) {
		this.groups.remove(group);
	}
	
	public void setGroupAtIndex(int index, Group group) {
		this.groups.get(index).equals(group);
	}
	
	public ArrayList<Group> getGroups() {
		return this.groups;
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
	 * Returns a string representation of the relationships that a faction currently has.
	 * @param faction
	 * @return {Key Value}, {Key Value} string which equals {factionName relationship}, {factionName relationship}
	 * @author ZettaX
	 */
	public static String getRelationshipString(Faction faction) {	
		return faction.relationships.toString();
	}
	public static String getRelationshipTypeString(Faction faction) {
		return faction.relationships.get(faction.getName()).toString();
	}
	public static relationshipTypes getRelationshipType(Faction faction) {
		return faction.relationships.get(faction.getName());
	}
	/**
	 * Sets a relationship between factions as a hashmap.
	 * @param faction1Name The faction name of the one setting the relationship
	 * @param faction2Name The faction name of the faction being set a relationship to
	 * @param type Enum relationshipTypes which currently has 6 values, ALLY, NEUTRAL, ENEMY, TRUCE, VASSAL, LORD
	 */
	public void setRelationshipByFactionName(String faction1Name, String faction2Name, relationshipTypes type) {
		if(Faction.getFactionByFactionName(faction1Name) == null || Faction.getFactionByFactionName(faction2Name) == null) {
			return;
		}
		Faction.getFactionByFactionName(faction1Name).relationships.put(faction2Name, type);
		Faction.getFactionByFactionName(faction2Name).relationships.put(faction1Name, type);
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
		return "name:" + name + ", dateCreated: " + dateCreated + ", members=" + returnListOfNames(getMembers()) + ", claimList: {"
				+ getArrayListOfCoordinates(this.claimList) + "}, groups: " + getListOfGroupNames(groups) + ", default group upon joining: "+this.defaultGroup.getName();
	}

	private ArrayList<String> getListOfGroupNames(ArrayList<Group> groups) {
		ArrayList<String> results = new ArrayList<String>();
		
		for ( Group group : groups) {
			results.add(group.getName());
		}
		
		return results;
	}

	public static String[] getArrayOfCoordinates(ArrayList<LandClaim> claims) {
		
		String[] array = new String[claims.size()];
		
		for ( int i = 0 ; i < array.length; i++) {
			array[i] =  (i==0) ? "[" : ",["+ claims.get(i).getChunkX() + ", " + claims.get(i).getChunkZ()+"]";
		}
		
		return array;
	}
	
	public static ArrayList<String> getArrayListOfCoordinates(ArrayList<LandClaim> claims) {
		ArrayList<String> coords = new ArrayList<String>();
		for ( LandClaim lc : claims) {
			String result = (lc.getClaimDescriptor().isEmpty()) ? "] " : "] - ``"+ lc.getClaimDescriptor() +"`` ";
			coords.add(" ["+lc.getChunkX() + ", " +lc.getChunkZ() + result);
		}
		return coords;
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
	 * Returns the list of visas currently in the faction
	 * @author ZettaX
	 * @return
	 */
	
	public ArrayList<Visa> getVisas() {
		return visas;
	}
	
	public void removeVisa(Visa visa) {
		this.visas.remove(visa);
	}
	
	public void addVisa(Visa visa) {
		this.visas.add(visa);
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

	/**
	 * Deserializes the faction from the specified *.fbin.
	 * Needs a nullchecl
	 * @param fileName the path to the file on disk
	 * @return faction object or null
	 */
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
	 * Returns the faction object that contains a specified member by UUID\
	 * Needs a nullcheck
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
	
	/**
	 * Retrieves a faction object representing a given faction
	 * after a valid name is specified. Requires a null check
	 * @param name
	 * @return faction object or NULL
	 */
	public static Faction getFactionByFactionName(String name) {
		for ( Faction faction1 : CustomNations.factions) {
			if (faction1.getName().equalsIgnoreCase(name)) {
				return faction1;
			}
		
		}
		return null;
	}
	
	/**
	 * Returns true if a faction exists, returns false if it does not
	 * @param name faction name
	 * @return boolean value as to whether the faction already exists
	 */
	public static boolean doesFactionExist(String name) {
		
		for (Faction fac : CustomNations.factions) {
			if (fac.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}

	public Group getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
	}

	/**
	 * gets the group that the player belongs to inside a given faction
	 * @param fac the faction that the player is in
	 * @param uniqueId 
	 * @return
	 */
	public static Group getGroupPlayerIsIn(Faction fac, UUID uniqueId) {
		Group result = null;
		for (Group group : fac.getGroups()) {
			for(UUID uuid : group.getMembers()) {
				if (uuid.equals(uniqueId)) {
					result = group;
				}
			}
		}
		return result;
	}

	/**
	 * Does this group exist in the faction?
	 * @param name name of the group
	 * @param fac faction in mind
	 * @return whether or not the group in-fact exists
	 */
	public static boolean doesGroupExist(String name, Faction fac) {
		
		for ( Group g : fac.getGroups()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Retrieves the matching group
	 * Requires nullcheck
	 * @param name name of the group
	 * @param fac faction that the group is in
	 * @return the group object (or null)
	 */
	public static Group getGroupFromFactionByName(String name, Faction fac) {
		
		for ( Group g : fac.getGroups()) {
			if (g.getName().equalsIgnoreCase(name)) {
				return g;
			}
		}
		return null;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}
}
