package openFactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.io.Serializable;
import java.time.Period;

public class Group implements Serializable {
	private String name;
	private ArrayList<UUID> members = new ArrayList<UUID>();
	private boolean joinable = false;
	private Period term = Period.ofDays(2);
	private boolean termsEnd = false;
	private int maxMembers = -1;
	
	private ArrayList<Can> groupPermissions = new ArrayList<Can>();
	
	public Group() {}
	public Group(String name) {
		this.name = name;
	}
	
	public Group(String name, boolean joinable, Period term, boolean termsEnd,
			int maxMembers,  Can... groupPermissions) {
		super();
		this.name = name;
		this.joinable = joinable;
		this.term = term;
		this.setTermsEnd(termsEnd);
		this.maxMembers = maxMembers;
		
		for (int i = 0; i < groupPermissions.length; i++) {
			this.groupPermissions.add(groupPermissions[i]);
		}
		
	}
	
	public Group(String name, ArrayList<UUID> members, boolean joinable, Period term, boolean termsEnd,
			int maxMembers, Can... groupPermissions) {
		super();
		this.name = name;
		this.members = members;
		this.joinable = joinable;
		this.term = term;
		this.setTermsEnd(termsEnd);
		this.maxMembers = maxMembers;
		
		for (int i = 0; i < groupPermissions.length; i++) {
			this.groupPermissions.add(groupPermissions[i]);
		}
		
	}
	
	public Group(String name, ArrayList<UUID> members, boolean joinable, Period term, boolean termsEnd,
			int maxMembers, ArrayList<Can> groupPermissions) {
		super();
		this.name = name;
		this.members = members;
		this.joinable = joinable;
		this.term = term;
		this.setTermsEnd(termsEnd);
		this.maxMembers = maxMembers;
		
		this.groupPermissions = groupPermissions;
		
	}
	
	@Override
	public String toString() {
		return "Group [name=" + name + ", members=" + members + ", joinable=" + joinable + ", term=" + term
				+ ", termsEnd=" + termsEnd + ", maxMembers=" + maxMembers + ", groupPermissions="
				+ (groupPermissions) + "]";
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
		return new Group( "admin",  false, Period.ZERO, false, 1, 
				Can.ASSIGN_GROUPS,	
				Can.CHANGE_FACTION_DESC,	
				Can.CHANGE_FACTION_NAME,	
				Can.CLAIM,	
				Can.DISBAND,
				Can.EDIT_CLAIM,	
				Can.EDIT_CLAIM_SETTINGS,
				Can.EDIT_GROUPS,
				Can.OVERRIDE_CLAIM_SETTINGS,
				Can.KICK,	
				Can.MAKE_OR_REMOVE_GROUPS,
				Can.SET_VISA,
				Can.UNCLAIM,	
				Can.UNCLAIM_ALL);
		
	}
	
	/**
	 * Creates a group of regular folk; 
	 * useful for setting a basic default group
	 * @return fully qualified group
	 */
	public static Group createCommonGroup() {
		return new Group( "common",  false, Period.ZERO, false, 1, 
				Can.CHANGE_FACTION_DESC,	
				Can.EDIT_CLAIM	
				);
	}
	
	public static boolean isPlayerInGroup(UUID uuid, Group group) {
		
		for ( UUID uniqueId : group.getMembers()) {
			if (uniqueId.equals(uuid)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean doesGroupHavePermission(Can can, Group group) {
//		for (Can permission : group.getGroupPermissions()) {
//			if (permission.equals(can)) {
//				return true;
//			}
//		}
		
		for ( int i = 0 ; i < group.getGroupPermissions().size(); i++) {
			if(group.getGroupPermissions().get(i) == can) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Group removeAllMembersFromGroup(Group group) {
		
//		for (UUID uuid : group.getMembers()) {
//			group.removeMember(uuid);
//		}
		
		for ( int i = 0 ; i < group.getMembers().size(); i++) {
			group.removeMember(group.getMembers().get(i));
		}
		
		Group result = group;
		return result;
	}
	
	public static boolean doesStringMatchAValidPermission(String string) {
		
		for ( int i = 0 ; i < Can.values().length; i++) {
			if (Can.values()[i].toString().equalsIgnoreCase(string)) {
				return true;
			} else if (Can.values()[i].toString().replace("_","").equalsIgnoreCase(string)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns new group, empties old group
	 * @param oldGroup group to have members transferred from
	 * @param newGroup group to have new members
	 * @return new group with members from the old group
	 */
	public static Group transferMembersFromGroup(Group oldGroup, Group newGroup) {
		
		newGroup.setMembers(oldGroup.getMembers());
		Group.removeAllMembersFromGroup(oldGroup);
		
		return newGroup;
	}
	
	public boolean isTermsEnd() {
		return termsEnd;
	}
	public void setTermsEnd(boolean termsEnd) {
		this.termsEnd = termsEnd;
	}
	
	public ArrayList<Can> getGroupPermissions() {
		return this.groupPermissions;
	}
	
	
	public void setGroupPermissions(ArrayList<Can> groupPermissions) {
		
		for (Can can : groupPermissions) {
			this.groupPermissions.add(can);
		}
		
		//this.groupPermissions = groupPermissions2;
	}
	
	public void addPermission(Can permission) {
		this.groupPermissions.add(permission);
	}
	
	public void removePermission(Can permission) {
		this.groupPermissions.remove(permission);
	}
	
	public boolean hasPermission(Can permission) {
		
		for (Can can : this.groupPermissions) {
			if (can.equals(permission)) {
				return true;
			}
		}
		
		return false;
	}
	
}
