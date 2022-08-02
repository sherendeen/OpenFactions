package openFactions.objects;

import java.io.Serializable;

public class Warp implements Serializable {
	
	private String warpName;
	private int warpX;
	private int warpY;
	private int warpZ;
	private String worldName;
	private Group associatedGroup;
	
	public Warp(String warpName, int warpX, int warpY, int warpZ,
			String worldName) {
		this.warpName = warpName;
		this.warpX = warpX;
		this.warpY = warpY;
		this.warpZ = warpZ;
		this.worldName = worldName;
	}
	
	public Warp(String warpName, int warpX, int warpY, int warpZ,
			String worldName, Group associatedGroup) {
		this.warpName = warpName;
		this.warpX = warpX;
		this.warpY = warpY;
		this.warpZ = warpZ;
		this.worldName = worldName;
		this.associatedGroup = associatedGroup;
	}

	public void setAssociatedGroup(Group group) {
		this.associatedGroup = group;
	}
	
	public Group getAssociatedGroup() {
		return associatedGroup;
	}
	
	public String getWarpName() {
		return warpName;
	}

	public void setWarpName(String warpName) {
		this.warpName = warpName;
	}

	public int getWarpX() {
		return warpX;
	}

	public void setWarpX(int warpX) {
		this.warpX = warpX;
	}

	public int getWarpY() {
		return warpY;
	}

	public void setWarpY(int warpY) {
		this.warpY = warpY;
	}

	public int getWarpZ() {
		return warpZ;
	}

	public void setWarpZ(int warpZ) {
		this.warpZ = warpZ;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	@Override
	public String toString() {
		return "Warp Name:" + warpName + ", coordinates " + warpX + ", " + warpY + ", " + warpZ
				+ ". World Name:" + worldName + ". Associated Group:" + associatedGroup + "";
	}
	
	
}
