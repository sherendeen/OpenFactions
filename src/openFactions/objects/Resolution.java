package openFactions.objects;

import openFactions.objects.enums.VoteThreshold;

public class Resolution {
	
	private String id = "";
	private String resolutionText;
	private String resolutionProposedBy;
	
	private String commandText;
	
	private VoteThreshold threshold = VoteThreshold.TwoThirds;
	
	private int yeaVotes = 0;
	private int nayVotes = 0;
	private int abstentions = 0;
	
	public Resolution(String id, String resolutionText, String resolutionProposedBy, String commandText, VoteThreshold threshold) {
		
	}

	@Override
	public String toString() {
		return "Resolution [id=" + id + ", resolutionText=" + resolutionText + ", resolutionProposedBy="
				+ resolutionProposedBy + ", threshold=" + threshold + ", yeaVotes=" + yeaVotes + ", nayVotes="
				+ nayVotes + ", abstentions=" + abstentions + "]";
	}
	
	
	
	
	
}
