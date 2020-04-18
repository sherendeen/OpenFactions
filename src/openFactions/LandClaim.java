package openFactions;

import org.bukkit.Chunk;

public class LandClaim {

	private Chunk claimedChunk;
	private String claimDescriptor;
	
	/**
	 * Empty constructor; does nothing
	 */
	public LandClaim() {
		
	}
	
	public LandClaim(Chunk claimedChunk) {
		this.claimedChunk = claimedChunk;
	}
	
	public void setClaimedChunk(Chunk claimedChunk) {
		this.claimedChunk = claimedChunk;
	}
	
	public Chunk getClaimedChunk() {
		return this.claimedChunk;
	}
	
	public void setClaimDescriptor(String claimDescriptor) {
		this.claimDescriptor = claimDescriptor;
	}
	
}
