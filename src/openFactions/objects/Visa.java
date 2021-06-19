/** 
Copyright (C) 2018-2020 Seth Herendeen; Samuel Inciarte

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
package openFactions;
**/
package openFactions.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Visa implements Serializable{
	
	private Date issueDate;
	private Date expirationDate;
	private String issuer;
	private UUID visaHolder;
	private int visaClass = 0;	
	
	/**
	 * Default Visa constructor
	 * @param issueDate The date this visa was issued.
	 * @param expirationDate The expiration date of this given visa.
	 * @param issuer The faction issuing this visa.
	 * @param uuid The player given this visa.
	 * @param visaClass Every Visa is class 0 by default.
	 */
	public Visa(Date issueDate, Date expirationDate, String issuer, UUID uuid, int visaClass) {
		this.setVisaClass(visaClass);
		this.setIssueDate(issueDate);
		this.setExpirationDate(expirationDate);
		this.setIssuer(issuer);
		this.setVisaHolder(uuid);
	}
	
	/**
	 * Visa without an expiration date. You will have to manually revoke this one.
	 * @param issueDate
	 * @param issuer
	 * @param visaHolder
	 * @param visaClass Every Visa is class 0 by default.
	 */
	public Visa(Date issueDate,  String issuer, UUID visaHolder, int visaClass) {
		this.setVisaClass(visaClass);
		this.setIssueDate(issueDate);
		this.setIssuer(issuer);
		this.setVisaHolder(visaHolder);
	}
	
	/**
	 * If the 1st parameter, the date the instant this method is called, happens to be before the expiration date of this visa, it will be revoked.
	 * It is worth noting that if expirationDate nil, a true value will be returned.
	 * @param currentDate 
	 * @param expirationDate
	 * @return a false value indicates that the visa has expired, and a true value indicates it is in good standing. 
	 */
	public boolean checkStatus(Date currentDate, Date expirationDate) {
		expirationDate = this.expirationDate;
		if(expirationDate == null) {
			return true;
		}
		if(currentDate.before(expirationDate) == true) {
			return false;
		}
		return true;
	}
		
	
	// Getters and setters below...
	
	public void setVisaClass(int visaClass) {
		this.visaClass = visaClass;
	}
	public void setVisaHolder(UUID visaHolder) {
		this.visaHolder = visaHolder;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public Date getIssueDate() {
		return issueDate;
	}
	public String getIssuer() {
		return issuer;
	}
	public UUID getVisaHolder() {
		return visaHolder;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public int getVisaClass() {
		return visaClass;
	}

	
}
