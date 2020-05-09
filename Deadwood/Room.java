import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Room {
	
	private ArrayList<String> neighbors; //a list of adjacent rooms
	private String name;
	private Scene sceneCard;
	private ArrayList<Role> roles; //off-card
	private int totalShots; //1-3
	private int remainingShots;
	
	public Room(Node s) {
		this.name = ((Element) s).getAttribute("name");
		NodeList neighbors = s.getFirstChild().getChildNodes();
		for(int i = 0; i < neighbors.getLength(); i++) {
			this.neighbors.add(((Element) neighbors.item(i)).getAttribute("name"));
		}
		this.totalShots = s.getChildNodes().getLength();
		this.remainingShots = this.totalShots;
		NodeList roles = s.getLastChild().getChildNodes();
		for(int i = 0; i < roles.getLength(); i++) {
			this.roles.add(new Role(roles.item(i)));
		}
	}

	// for the Trailers and Casting Office rooms
	public Room(Node s, String name) {
		this.name = name;
		NodeList roles = s.getFirstChild().getChildNodes();
	}

	/* Getters */
	public ArrayList<String> getNeighbors() {
		return this.neighbors;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Scene getSceneCard() {
		return this.sceneCard;
	}
	
	public ArrayList<Role> getRoles() {
		return this.roles;
	}
	
	public int getTotalShots() {
		return this.totalShots;
	}
	
	public int getRemainingShots() {
		return this.remainingShots;
	}
	
	/* Setters */
	public void setNeighbor(String neighbor) {
		this.neighbors.add(neighbor);
	}
	
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
		String result = this.getNeighbors().get(0);
		for(int i = 1; i < this.getNeighbors().size(); i++) {
			result += ", " + this.getNeighbors().get(i);
		}
		return result;
	}
	
	/* Lists the name of each role in this.roles in a comma-separated list */
	public String listRoles(int rank) {
		String result = "";
		if(this.getRoles().get(0).getRank() <= rank) {
			result = this.getRoles().get(0).getName();
		}
		for(int i = 1; i < this.getRoles().size(); i++) {
			if(this.getRoles().get(i).getRank() <= rank) {
				result += ", " + this.getRoles().get(i).getName();
			}
		}
		return result;
	}
}
