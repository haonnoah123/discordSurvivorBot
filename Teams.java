package survivor;

import net.dv8tion.jda.api.entities.Role;

public class Teams {

	private Role role;
	private int idolNumber;
	private boolean isIdolFound;
	
	public Teams(Role teamRole) {
		role = teamRole;
		idolNumber = (int)(Math.random() * 100 + 1);
		isIdolFound = false;
	}
	
	public Teams(Role role, int idolNumber, boolean isIdolFound) {
		this.role = role;
		this.idolNumber = idolNumber;
		this.isIdolFound = isIdolFound;
	}
	
	public void idolFound() {
		isIdolFound = true;
	}
	
	public boolean getIsIdolFound() {
		return isIdolFound;
	}
		
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getIdolNumber() {
		return idolNumber;
	}

	public void resetIdolNumber() {
		this.idolNumber = (int)(Math.random() * 100 + 1);
		isIdolFound = false;
	}
	
	public String toString() {
		return role + "\n" + idolNumber + "\n" + isIdolFound + "\n" + "\n";
	}

}
