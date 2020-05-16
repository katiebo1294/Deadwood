import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Role implements Comparable< Role > {

	private String name;
	private int rank; // 1-6
	private String line;
	private Location location; // scene name or room name
	private Player actor;
	
	public Role(Node role) {
		this.name = ((Element) role).getAttribute("name");
		this.rank = Integer.parseInt(((Element) role).getAttribute("level"));
		this.line = "\"" + ((Element) role).getElementsByTagName("line").item(0).getTextContent() + "\"";
	}
	
	/* Getters */
	public int getRank() {
		return this.rank;
	}
	public void setRank(int rank){
        this.rank = rank;
	}
	
	public boolean isWorked() {
		return this.actor != null;
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
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(Location location){
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
