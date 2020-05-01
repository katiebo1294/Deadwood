import java.util.ArrayList;

public class Room {
	
	private ArrayList<Room> neighbors; //a list of adjacent rooms
	private String name;
	private Scene sceneCard;
	private ArrayList<Role> roles; //off-card
	private int totalShots; //1-3
	private int remainingShots;

	public Room(String name, ArrayList<Role> roles, int totalShots) {
		this.name = name;
		this.roles = roles;
		this.totalShots = totalShots;
		this.remainingShots = totalShots;
		neighbors = new ArrayList<Room>();
	}
	
	/* Getters */
	public ArrayList<Room> getNeighbors() {
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
	public void setNeighbor(Room neighbor) {
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
		String result = this.getNeighbors().get(0).getName();
		for(int i = 1; i < this.getNeighbors().size(); i++) {
			result += ", " + this.getNeighbors().get(i).getName();
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
