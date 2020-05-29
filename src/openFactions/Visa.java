package openFactions;

public class Visa {
	
	private String issueDate;
	private String expirationDate;
	private String issuer;
	private String visaHolder;
	private int visaClass = 0;
	
	/**
	 * Default Visa constructor
	 * @param issueDate The date this visa was issued.
	 * @param expirationDate The expiration date of this given visa.
	 * @param issuer The faction issuing this visa.
	 * @param visaHolder The player given this visa.
	 * @param visaClass Every Visa is class 0 by default.
	 */
	public Visa(String issueDate, String expirationDate, String issuer, String visaHolder, int visaClass) {
		this.setVisaClass(visaClass);
		this.setIssueDate(issueDate);
		this.setExpirationDate(expirationDate);
		this.setIssuer(issuer);
		this.setVisaHolder(visaHolder);
	}
	/**
	 * Visa without an expiration date. You will have to manually revoke this one.
	 * @param issueDate
	 * @param issuer
	 * @param visaHolder
	 * @param visaClass Every Visa is class 0 by default.
	 */
	public Visa(String issueDate,  String issuer, String visaHolder, int visaClass) {
		this.setVisaClass(visaClass);
		this.setIssueDate(issueDate);
		this.setIssuer(issuer);
		this.setVisaHolder(visaHolder);
	}
	
	public void setVisaClass(int visaClass) {
		this.visaClass = visaClass;
	}
	
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getVisaHolder() {
		return visaHolder;
	}
	public void setVisaHolder(String visaHolder) {
		this.visaHolder = visaHolder;
	}
	
}
