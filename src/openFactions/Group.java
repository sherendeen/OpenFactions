package openFactions;

import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import java.time.Period;

public class Group implements Serializable {
	private String name;
	private ArrayList<UUID> members;
	private boolean joinable = false;
	private Period term = Period.ofDays(2);
	private boolean termsEnd = false;
	private int maxMembers = -1;
	private boolean canEditEverything = false;
	
	public Group() {}
	public Group(String name) {
		this.name = name;
	}
	
	public Group(String name, boolean joinable, Period term, boolean termsEnd,
			int maxMembers, boolean canEditEverything) {
		super();
		this.name = name;
		this.joinable = joinable;
		this.term = term;
		this.termsEnd = termsEnd;
		this.maxMembers = maxMembers;
		this.canEditEverything = canEditEverything;
	}
	
	public Group(String name, ArrayList<UUID> members, boolean joinable, Period term, boolean termsEnd,
			int maxMembers, boolean canEditEverything) {
		super();
		this.name = name;
		this.members = members;
		this.joinable = joinable;
		this.term = term;
		this.termsEnd = termsEnd;
		this.maxMembers = maxMembers;
		this.canEditEverything = canEditEverything;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<UUID> getMembers() {
		return members;
	}
	
	public void setMembers(ArrayList<UUID> members) {
		this.members = members;
	}
	
	public boolean isJoinable() {
		return joinable;
	}
	
	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}
	
	public Period getTerm() {
		return term;
	}
	
	public void setTerm(Period term) {
		this.term = term;
	}
	
	public int getMaxMembers() {
		return maxMembers;
	}
	
	public void setMaxMembers(int maxMembers) {
		this.maxMembers = maxMembers;
	}
	
	public void addMember(UUID member) {
		this.members.add(member);
	}
	
	public void removeMember(UUID member) {
		this.members.remove(member);
	}
	
	/**
	 * Creates a preset group for administrators/despots
	 * @return fully qualified group
	 */
	public static Group createAdminGroup() {
		return new Group( "admin",  false, Period.ZERO, false,
				1, true);
	}
	/**
	 * Creates a group of regular folk; 
	 * useful for setting a basic default group
	 * @return fully qualified group
	 */
	public static Group createCommonGroup() {
		return new Group("common", false, Period.ZERO,false, -1, false);
	}
	
}
