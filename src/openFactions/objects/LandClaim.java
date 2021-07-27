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

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class LandClaim implements Serializable {

	private transient Chunk claimedChunk;
	private String claimDescriptor;
	private int chunkX;
	private int chunkZ;
	private Group exclusiveGroup = null;
	private String worldName;
	
	/**
	 * Empty constructor; resets chunk value if possible
	 */
	public LandClaim() {
		
	}
	
	
	/**
	 * Sets the landclaim with the chunk. Claim descriptor is an empty string
	 * @param claimedChunk the minecraft chunk in question
	 */
	public LandClaim(Chunk claimedChunk) {
		this.claimDescriptor = "";
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.setWorldName(claimedChunk.getWorld().getName());
	}
	
	public LandClaim(Chunk claimedChunk, String claimDescriptor) {
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.setWorldName(claimedChunk.getWorld().getName());
		this.claimDescriptor = claimDescriptor;
		
	}
	
	public LandClaim(Chunk claimedChunk, String claimDescriptor, 
			Group exclusiveGroup) {
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.setWorldName(claimedChunk.getWorld().getName());
		this.claimDescriptor = claimDescriptor;
		this.exclusiveGroup = exclusiveGroup;
	}
	
	public LandClaim(Chunk claimedChunk, String claimDescriptor, 
			Group exclusiveGroup, 
			String worldName) {
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.claimDescriptor = claimDescriptor;
		this.exclusiveGroup = exclusiveGroup;
		this.worldName = worldName;
	}
	
	public void setClaimDescriptor(String claimDescriptor) {
		this.claimDescriptor = claimDescriptor;
	}
	
	public String getClaimDescriptor() {
		return this.claimDescriptor;
	}

	public int getChunkX() {
		return chunkX;
	}


	public void setChunkX(int chunkX) {
		this.chunkX = chunkX;
	}


	public int getChunkZ() {
		return chunkZ;
	}


	public void setChunkZ(int chunkZ) {
		this.chunkZ = chunkZ;
	}


	public Chunk getClaimedChunk() {
		return claimedChunk;
	}


	public void setClaimedChunk(Chunk claimedChunk) {
		this.claimedChunk = claimedChunk;
	}
	
	/**
	 * Since we can't serialize chunks, we must serialize 
	 * chunk coordinates and world names.
	 * Use of this method is necessary so that we can
	 *  re-correlate chunks with coordinates upon startup
	 * @param x  x chunk coordinate
	 * @param z z chunk coordinate
	 * @param worldName the name of the world as a string
	 */
	public void setClaimedChunkFromCoordinates(int x, int z, String worldName) {
//		World w = pluginRef.getWorld();
		
		World w = Bukkit.getWorld(worldName);
		this.claimedChunk = w.getChunkAt(x, z);
	}

	public Group getExclusiveGroup() {
		return exclusiveGroup;
	}


	public void setExclusiveGroup(Group exclusiveGroup) {
		this.exclusiveGroup = exclusiveGroup;
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}
	
	@Override
	public String toString() {
		return "LandClaim [claimDescriptor=" +(( claimDescriptor!=null ) ? claimDescriptor : "No description provided.")+ ", chunk coordinates: [" + chunkX + ", " + chunkZ
				+ "], Exclusive Group: " + ((exclusiveGroup!=null) ? exclusiveGroup.getName():"n/a"  )+ ". What world it is on: " + worldName + ".";
	}
	
	
}
