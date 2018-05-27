package openFactions;

import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.World;

/**
 * 
 * 
 *
 */
public class LandClaim extends Chunk {
	
	private double x;
	private double y;
	
	public LandClaim(World world, int i, int j) {
		super(world, i, j);
		this.x = x;
		this.y = y;
	}
	
	public double getChunkX() {
		return this.x;
	}
	
	public double getChunkY() {
		return this.y;
	}
	
}
