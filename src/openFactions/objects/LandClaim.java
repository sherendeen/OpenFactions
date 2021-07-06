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

import org.bukkit.Chunk;
import org.bukkit.World;

import openFactions.CustomNations;

public class LandClaim implements Serializable {

	private transient Chunk claimedChunk;
	private String claimDescriptor;
	private int chunkX;
	private int chunkZ;
	private Group exclusiveGroup = null;
	
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
	}
	
	public LandClaim(Chunk claimedChunk, String claimDescriptor) {
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.claimDescriptor = claimDescriptor;
	}
	
	public LandClaim(Chunk claimedChunk, String claimDescriptor, 
			Group exclusiveGroup) {
		this.setChunkX(claimedChunk.getX());
		this.setChunkZ(claimedChunk.getZ());
		this.setClaimedChunk(claimedChunk);
		this.claimDescriptor = claimDescriptor;
		this.setExclusiveGroup(exclusiveGroup);
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
	 * Since we can't serialize chunks, we must serialize chunk coordinates.
	 * Use of this method is necessary so that we can
	 *  re-correlate chunks with coordinates upon startup
	 * @param x 
	 * @param z
	 * @param pluginRef
	 */
	public void setClaimedChunkFromCoordinates(int x, int z, CustomNations pluginRef) {
		World w = pluginRef.getWorld();
		this.claimedChunk = w.getChunkAt(x, z);
	}


	public Group getExclusiveGroup() {
		return exclusiveGroup;
	}


	public void setExclusiveGroup(Group exclusiveGroup) {
		this.exclusiveGroup = exclusiveGroup;
	}
}
