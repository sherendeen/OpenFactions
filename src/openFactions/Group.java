package openFactions;

import java.util.ArrayList;
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
		this.name = name;
		this.members = members;
		this.joinable = joinable;
		this.term = term;
		this.setTermsEnd(termsEnd);
		this.maxMembers = maxMembers;
		
		this.groupPermissions = groupPermissions;
		
	}
	
	public Group(String name, boolean joinable, Period term, boolean termsEnd,
			int maxMembers, ArrayList<Can> groupPermissions) {
		this.name = name;
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
		System.out.println("Adding ["+permission.toString()+"] permission from " + this.name + " ");
		this.groupPermissions.add(permission);
	}
	
	public void removePermission(Can permission) {
		System.out.println("Removing ["+permission.toString()+"] permission from " + this.name + " ");
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
