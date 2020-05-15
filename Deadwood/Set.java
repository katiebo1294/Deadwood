import java.util.Arrays;

import org.w3c.dom.Element;

public class Set extends Room {

	private Scene sceneCard;
	private Role[] roles; //off-card
	private int totalShots; //1-3
	private int remainingShots;
	
	public Set(Element set) {
		super(set);
		this.name = set.getAttribute("name");
		this.totalShots = ((Element) set.getElementsByTagName("takes").item(0)).getElementsByTagName("take").getLength();
		this.remainingShots = this.totalShots;
		this.roles = new Role[((Element) set.getElementsByTagName("parts").item(0)).getElementsByTagName("part").getLength()];
		for(int i = 0; i < roles.length; i++) {
			this.roles[i] = new Role(((Element) set.getElementsByTagName("parts").item(0)).getElementsByTagName("part").item(i));
		}
	}

	
	/* Getters */
	public Scene getSceneCard() {
		return this.sceneCard;
	}
	
	public Role[] getRoles() {
		return this.roles;
	}
	
	public int getTotalShots() {
		return this.totalShots;
	}
	
	public int getRemainingShots() {
		return this.remainingShots;
	}
	
	/* Setters */
	
	public void setScene(Scene scene) {
		this.sceneCard = scene;
	}
	
	public void replaceShots() {
		this.remainingShots = totalShots;
	}
	
	public void removeShot() {
		this.remainingShots--;
	}
	
	public void removeSceneCard() {
		this.sceneCard = null;
	}
	
	// Lists the name of each role in this.roles in a comma-separated list
	public String listAvailableRoles(int rank) {
		String result = "";
		if(this.roles[0].getRank() <= rank) {
			result = this.roles[0].getName();
		}
		for(int i = 1; i < this.roles.length; i++) {
			if(this.roles[i].getRank() <= rank) {
				result += ", " + this.roles[i].getName();
			}
		}
		return result;
	}
	
	public String listRoles() {
		if(this.getRoles().length > 0) {
			String result = "[" + this.roles[0].toString();
			for(int i = 1; i < this.roles.length; i++) {
				result += ", " + this.roles[i].toString();
			}
			result += "]";
			return result;
		} else {
			return "[none]";
		}
	}
	
	public String toString() {
		 return "name = '" + this.name + "', neighbors = " + super.listNeighbors() + ", roles = " + this.listRoles() + ", total shots = " + this.totalShots;
	}
}
