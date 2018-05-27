package openFactions;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Seth G. R. Herendeen [inivican]
 *
 */
public class Faction {

	private String name;
	private Date dateCreated;	
	private ArrayList<String> members;
	private ArrayList<LandClaim> claimList;
	
	public Faction(String name, Date dateCreated, ArrayList<String> members, ArrayList<LandClaim> claimList) {
		
		//TODO: Save parameters to file once a new faction object is created
		
	}
	/**
	 * Constructor for faction that doesn't assume too much of the user
	 * @param name the name of the faction
	 * @param personWhoCreatedTheFaction self commenting, self explanatory
	 */
	public Faction(String name, String personWhoCreatedTheFaction) {
		this.dateCreated = new Date();
		this.members.add(personWhoCreatedTheFaction);
	}
	
	/**
	 * Returns the name of the faction
	 * @author ZettaX
	 * @return String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns the date the faction was created
	 * @author ZettaX
	 * @return String
	 */
	public Date getCreationDate() {
		return dateCreated;
	}
	/**
	 * Returns the list of members currently in the faction
	 * @author ZettaX
	 * @return String (ArrayList)
	 */
	public ArrayList<String> getMembers() {
		return members;
	}
	/**
	 * Returns the faction's list of claims
	 * @author ZettaX
	 * @return LandClaim (ArrayList)
	 */
	public ArrayList<LandClaim> getClaims() {
		return claimList;
	}
	
}
