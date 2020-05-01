
public class Role implements Comparable< Role > {

	private int rank; // 1-6
	private boolean isWorked; // if the role is currently being worked by a player
	private String name;
	private String line;
	private String location; // scene name or room name
	private Player actor;
	
	public Role(int rank, String name, String line, String location) {
		this.rank = rank;
		this.isWorked = false;
		this.name = name;
		this.line = line;
		this.location = location;
	}
	
	/* Getters */
	public int getRank() {
		return this.rank;
	}
	
	public boolean getIsWorked() {
		return this.isWorked;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public Player getActor() {
		return this.actor;
	}
	
	public void setActor(Player p) {
		this.actor = p;
	}
	
	// for sorting roles by rank when calculating end-of-scene payout
	@Override
	public int compareTo(Role r) {
		return r.getRank()-this.rank;
	} 

}
