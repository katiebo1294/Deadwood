import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

// TODO: maybe create subclasses for Set, Trailers, and Casting Office? 
public class Room {
	
	private String[] neighbors; //a list of adjacent rooms
	private String name;
	private Scene sceneCard;
	private Role[] roles; //off-card
	private int totalShots; //1-3
	private int remainingShots;
	
	public Room(Element s) {
		this.name = s.getAttribute("name");
		this.neighbors = new String[((Element) s.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").getLength()];
		for(int i = 0; i < neighbors.length; i++) {
			String neighborName = ((Element) ((Element) s.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").item(i)).getAttribute("name");
			if(neighborName.equals("office")) {
				neighbors[i] = "Casting Office";
			} else if(neighborName.equals("trailer")) {
				neighbors[i] = "Trailers";
			} else {
				neighbors[i] = neighborName;
			}
		}
		this.totalShots = ((Element) s.getElementsByTagName("takes").item(0)).getElementsByTagName("take").getLength();
		this.remainingShots = this.totalShots;
		this.roles = new Role[((Element) s.getElementsByTagName("parts").item(0)).getElementsByTagName("part").getLength()];
		for(int i = 0; i < roles.length; i++) {
			this.roles[i] = new Role(((Element) s.getElementsByTagName("parts").item(0)).getElementsByTagName("part").item(i));
		}
	}

	// for the Trailers and Casting Office rooms
	public Room(Element s, String name) {
		this.name = name;
		this.neighbors = new String[((Element) s.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").getLength()];
		for(int i = 0; i < neighbors.length; i++) {
			String neighborName = ((Element) ((Element) s.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").item(i)).getAttribute("name");
			if(neighborName.equals("office")) {
				neighbors[i] = "Casting Office";
			} else if(neighborName.equals("trailer")) {
				neighbors[i] = "Trailers";
			} else {
				neighbors[i] = neighborName;
			}
		}
		this.roles = new Role[0];
	}

	/* Getters */
	public String[] getNeighbors() {
		return this.neighbors;
	}
	
	public String getName() {
		return this.name;
	}
	
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
	
	/* Lists the name of each adjacent room in this.neighbors in a comma-separated list */
	public String listNeighbors() {
		return Arrays.toString(this.neighbors);
	}
	
	/* Lists the name of each role in this.roles in a comma-separated list */
	public String listAvailableRoles(int rank) {
		String result = "";
		if(this.roles[0].getRank() <= rank) {
			result = this.roles[0].getName();
		}
		for(int i = 1; i < this.roles.length; i++) {
			if(this.roles[0].getRank() <= rank) {
				result += ", " + this.roles[0].getName();
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
		return "name = '" + this.name + "', neighbors = " + this.listNeighbors() + ", roles = " + this.listRoles() + ", total shots = " + this.totalShots;
	}
}
