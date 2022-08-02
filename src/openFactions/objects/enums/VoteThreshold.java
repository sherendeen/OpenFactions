package openFactions.objects.enums;

/**
 * Vote thresholds required for resolutions to pass
 * <ul>
 * 	<li>two thirds (simple majority)</li>
 * 	<li>three fifths (super majority)</li>
 * 	<li>unanimous (every vote must be in favor of resolution)</li>
 * </ul>
 *
 */
public enum VoteThreshold {
	/**
	 * 2/3rds majority of cast votes are required for resolution to pass
	 */
	TwoThirds, 
	/**
	 * 3/5ths (super majority) of cast votes are required for resolution to pass
	 */
	ThreeFifths, 
	/**
	 * Every vote must be in favor of the resolution
	 */
	Unanimous
}
