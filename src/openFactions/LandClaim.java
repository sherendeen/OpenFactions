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

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class LandClaim implements Serializable {

	private transient Chunk claimedChunk;
	private Location location;
	private String claimDescriptor;
	private int chunkX;
	private int chunkZ;
	
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
	
	public void setClaimDescriptor(String claimDescriptor) {
		this.claimDescriptor = claimDescriptor;
	}
	
	public String getClaimDescriptor() {
		return this.claimDescriptor;
	}
	
	/**
	 * determines whether a given landclaim exists in any claims' list
	 * of any particular faction
	 * @param lc the specified land claim
	 * @return true if the land in particular is claimed; false if not
	 */
	public static boolean isSpecifiedLandClaimInsideAnyFaction(LandClaim lc) {
		
		for (Faction fac : CustomNations.factions) {
			for (LandClaim landClaim : fac.getClaims()) {
				if (lc.equals(landClaim)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Does this chunk exist inside ANY faction?
	 * @param chunk the specified chunk
	 * @return whether or not any faction has this chunk
	 */
	public static boolean isSpecifiedChunkInsideAnyFaction(Chunk chunk) {
		
		for (Faction fac : CustomNations.factions) {
			for (LandClaim landClaim : fac.getClaims()) {
				if (landClaim.getClaimedChunk().equals(chunk)) {
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	/**
	 * returns faction objects where a specific chunk is inside of
	 * @param chunk minecraft chunk (which may be encapsulated in a LandClaim)
	 * @return list of factions
	 */
//	public static ArrayList<Faction> returnFactionObjectsWhereChunkIsFoundIn(Chunk chunk){
//		ArrayList<Faction> result = new ArrayList<Faction>();
//		
//		for (Faction fac : CustomNations.factions) {
//			for (LandClaim landClaim : fac.getClaims()) {
//				//is this chunk inside a landclaim that is inside a faction
//				//in a list of factions?
//				//if so, add this faction to the result list
//				if (landClaim.getCraftChunk().equals(chunk)) {
//					result.add(fac);
//				}
//			}
//		}
//		
//		return result;
//		
//	}
	
	/**
	 * returns faction objects where a specific chunk is inside of
	 * @param chunk minecraft chunk (which may be encapsulated in a LandClaim)
	 * @return list of factions
	 */
	public static ArrayList<Faction> returnFactionObjectsWhereChunkIsFoundIn(Chunk chunk){
		ArrayList<Faction> result = new ArrayList<Faction>();
		for (Faction fac : CustomNations.factions) {
			for (LandClaim landClaim : fac.getClaims()) {
				//is this chunk inside a landclaim that is inside a faction
				//in a list of factions?
				//if so, add this faction to the result list
				if (landClaim.getClaimedChunk().equals(chunk)) {
					result.add(fac);
				}
			}
		}
		
		return result;
		
	}
	
	/**
	 * returns the encapsulating landclaim object containing
	 * a chunk. Requires NULL CHECK!
	 * @param chunk the queried chunk
	 * @return landclaim object containing the chunk; or null.
	 */
	public static LandClaim returnLandClaimContainingSpecifiedChunk(Chunk chunk) {
		for (Faction fac : CustomNations.factions) {
			for (LandClaim landClaim : fac.getClaims()) {
				// is this chunk exactly equal to the one 
				//encapsulated in this particular landclaim object????
				if (landClaim.getClaimedChunk().equals(chunk)) {
					//match found!
					return landClaim;
				}
			}
		}
		return null;
	}
	
	/**
	 * returns an arraylist containing the faction object(s)
	 * that claim this specified land claim.
	 * 
	 * Because multiple factions can contest a given area of land, it is
	 * thus 
	 * @param lc
	 * @return
	 */
	public static ArrayList<Faction> returnFactionObjectsWhereClaimIsFoundIn(LandClaim lc) {
		ArrayList<Faction> result = new ArrayList<Faction>();
		for (Faction fac : CustomNations.factions) {
			for (LandClaim landClaim : fac.getClaims()) {
				// if the land claim is an exact match
				if (lc.equals(landClaim)) {
					//match found!
					result.add( fac );
				}
			}
		}
		
		return result;
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
}
