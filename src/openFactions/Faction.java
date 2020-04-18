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

/**
 * 
 * @author Seth G. R. Herendeen [inivican]
 *
 */
public class Faction implements Serializable {

	private String name;
	private String dateCreated;
	private ArrayList<String> members = new ArrayList<String>();
	private ArrayList<LandClaim> claimList = new ArrayList<LandClaim>();
	private UUID serialUUID;

	public Faction(String name, Date dateCreated, ArrayList<String> members, ArrayList<LandClaim> claimList) {

		this.name = name;
		this.dateCreated = dateCreated.toString();
		this.members = members;
		this.claimList = claimList;

		this.serialUUID = UUID.randomUUID();
		
	}
	
	public Faction(String name, String dateCreatedStr, ArrayList<String> members, ArrayList<LandClaim> claims) {
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
	public Faction(String name, String personWhoCreatedTheFaction) {
		this.dateCreated = new Date().toString();
		this.members.add(personWhoCreatedTheFaction);
	}
	
	/**
	 * Returns the readonly UUID. Must stay unique.
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
	public ArrayList<String> getMembers() {
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

	public static Faction makeTestFaction() {

		Faction fac = new Faction("Testificate", "inivican");
		fac.members.add("ZettaX");
		fac.members.add("Gagger");

		return fac;
	}

	/**
	 * Saves the FACTION INSTANCE to a binary file
	 * @param faction instance of the faction (i.e the faction you want to save)
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
			System.out.println("Serialization of the faction object" + 
		faction.serialUUID + " ("+ faction.name +") to ["+fileName+"] did not go well.");
		}
	}
	
	public static Faction deserialize(String fileName) {
		Faction faction = null;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			//attempt to read and deserialize
			faction = (Faction) objectInputStream.readObject();
			
			objectInputStream.close();
			fileInputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Deserialization of the faction object from ["+fileName+"] did not go well.");
		}
		
		return faction;
	}


}
