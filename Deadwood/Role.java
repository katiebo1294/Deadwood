
public class Role implements Comparable< Role > {

	private int rank; // 1-6
	private boolean isWorked; // if the role is currently being worked by a player
	private String name;
	private String line;
	private String location; // scene name or room name
	private Player actor;
	
	public Role(){}
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
	public void setRank(int rank){
        this.rank = rank;
	}
	
	public boolean getIsWorked() {
		return this.isWorked;
	}
	public void setIsWorked(boolean isWorked){
        this.isWorked = isWorked;
	}
	public String getName() {
		return this.name;
	}
	
	public void setName(String name){
        this.name = name;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public void setLine(String line){
        this.line = line;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public void setLocation(String location){
        this.location = location;
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
