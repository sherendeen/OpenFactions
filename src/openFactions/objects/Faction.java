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

package openFactions.objects;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import openFactions.objects.enums.RelationshipType;
import openFactions.util.Helper;

public class Faction implements Serializable {

	/**
	 * serial version of this faction object and related objects
	 */
	private static final long serialVersionUID = 901448862403914419L;

	private String name;
	
	private Date dateCreated;
	private Date dateOfLastLogin;
	
	private ArrayList<Visa> visas = new ArrayList<Visa>();
	private String desc;
	private ArrayList<UUID> members = new ArrayList<UUID>();
	
	private ArrayList<Warp> warps = new ArrayList<Warp>();
	
	private HashMap<String, RelationshipType> relationships = new HashMap<String, RelationshipType>(); 

	private ArrayList<Resolution> currentResolutions = new ArrayList<Resolution>();
	
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

	/**
	 * if true, players may join the faction without being manually added
	 * by a faction member. OFF by default.
	 * Can be toggled with /of joinable
	 */
	private boolean isJoinable = false;
	
	public Faction(String name, Date dateCreated, ArrayList<UUID> members, ArrayList<LandClaim> claims) {
		
		this.name = name;
		this.dateCreated = dateCreated;
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
		this.dateCreated = new Date();
		this.name = name;
		this.members.add(personWhoCreatedTheFaction);
		
		Group adminGroup = Helper.createAdminGroup();
		Group commons = Helper.createCommonGroup();
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
	public static RelationshipType getRelationshipType(Faction faction) {
		return faction.relationships.get(faction.getName());
	}
	/**
	 * Sets a relationship between factions as a hashmap.
	 * @param faction1Name The faction name of the one setting the relationship
	 * @param faction2Name The faction name of the faction being set a relationship to
	 * @param type Enum relationshipTypes which currently has 6 values, ALLY, NEUTRAL, ENEMY, TRUCE, VASSAL, LORD
	 */
	public void setRelationshipByFactionName(String faction1Name, String faction2Name, RelationshipType type) {
		if(Helper.getFactionByFactionName(faction1Name) == null || Helper.getFactionByFactionName(faction2Name) == null) {
			return;
		}
		Helper.getFactionByFactionName(faction1Name).relationships.put(faction2Name, type);
		Helper.getFactionByFactionName(faction2Name).relationships.put(faction1Name, type);
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
		return "name:" + name + ", description: " + desc + ", dateCreated: " + dateCreated + ", members=" + Helper.returnListOfNames(this.members) + ", claimList: {"
				+ Helper.getArrayListOfCoordinates(this.claimList) + "}, groups: " + getListOfGroupNames(groups) + ", default group upon joining: "+this.defaultGroup.getName();
	}

	public ArrayList<String> getListOfGroupNames(ArrayList<Group> groups) {
		ArrayList<String> results = new ArrayList<String>();
		
		for ( Group group : groups) {
			results.add(group.getName());
		}
		
		return results;
	}

	/**
	 * Returns the date the faction was created
	 * 
	 * @author ZettaX
	 * @return String
	 */
	public Date getCreationDate() {
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
		return "OpenFactions/faction_" + getSerialUUID() + "_.fbin";
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
	
	
	public HashMap<String, RelationshipType> getRelationships() {
		return relationships;
	}

	public void setRelationships(HashMap<String, RelationshipType> relationships) {
		this.relationships = relationships;
	}

	public Group getDefaultGroup() {
		return defaultGroup;
	}

	public void setDefaultGroup(Group defaultGroup) {
		this.defaultGroup = defaultGroup;
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

	public ArrayList<Warp> getWarps() {
		return warps;
	}

	public void setWarps(ArrayList<Warp> warps) {
		this.warps = warps;
	}
	
	public void addWarp(Warp warp) {
		this.warps.add(warp);
	}
	
	public void removeWarp(Warp warp) {
		this.warps.remove(warp);
	}

	public ArrayList<Resolution> getCurrentResolutions() {
		return currentResolutions;
	}

	public void setCurrentResolutions(ArrayList<Resolution> currentResolutions) {
		this.currentResolutions = currentResolutions;
	}
	
	public void addResolution(Resolution resolution) {
		this.currentResolutions.add(resolution);
	}
	
	public void removeResolution(Resolution resolution) {
		this.currentResolutions.remove(resolution);
	}

	public Date getDateOfLastLogin() {
		return dateOfLastLogin;
	}

	public void setDateOfLastLogin(Date dateMemberWasLastOnline) {
		this.dateOfLastLogin = dateMemberWasLastOnline;
	}

	public boolean isJoinable() {
		return isJoinable;
	}

	public void setJoinable(boolean isJoinable) {
		this.isJoinable = isJoinable;
	}
}
