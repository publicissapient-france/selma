package fr.xebia.extras.selma.beans;

/**
 * @author FaniloRandria
 */
public class Phone {
	
	private String phoneNumber;
	
	public Phone (String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
