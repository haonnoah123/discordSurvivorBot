package survivor;

import net.dv8tion.jda.api.entities.Role;

public class Teams {

	private long roleId;
	private int idolNumber;
	private boolean isIdolFound;
	
	public Teams(long teamRole) {
		roleId = teamRole;
		idolNumber = (int)(Math.random() * 100 + 1);
		isIdolFound = false;
	}
	
	public Teams(long role, int idolNumber, boolean isIdolFound) {
		this.roleId = role;
		this.idolNumber = idolNumber;
		this.isIdolFound = isIdolFound;
	}
	
	public void idolFound() {
		isIdolFound = true;
	}
	
	public boolean getIsIdolFound() {
		return isIdolFound;
	}
		
	public long getRoleId() {
		return roleId;
	}

	public void setRole(long role) {
		this.roleId = role;
	}

	public int getIdolNumber() {
		return idolNumber;
	}

	public void resetIdolNumber() {
		this.idolNumber = (int)(Math.random() * 100 + 1);
		isIdolFound = false;
	}
	
	public String toString() {
		return roleId + "\n" + idolNumber + "\n" + isIdolFound + "\n" + "\n";
	}

}
