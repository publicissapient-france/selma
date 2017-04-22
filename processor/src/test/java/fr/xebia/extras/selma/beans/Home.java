package fr.xebia.extras.selma.beans;

/**
 * @author FaniloRandria
 */
public class Home {

	private Phone phoneHome;
	
	public Home (Phone phoneHome) {
		this.phoneHome = phoneHome;
	}

	public Phone getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(Phone phoneHome) {
		this.phoneHome = phoneHome;
	}
	
}
