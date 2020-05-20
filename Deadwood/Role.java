import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Role implements Comparable< Role > {

	private String name;
	private int rank; // 1-6
	private int[] area; //x, y, h, w
	private String line;
	private Player actor;
	
	public Role(Node role) {
		this.name = ((Element) role).getAttribute("name");
		this.rank = Integer.parseInt(((Element) role).getAttribute("level"));
		this.area = new int[4];
		this.area[0] = Integer.parseInt(((Element) ((Element) role).getElementsByTagName("area").item(0)).getAttribute("x"));
		this.area[1] = Integer.parseInt(((Element) ((Element) role).getElementsByTagName("area").item(0)).getAttribute("y"));
		this.area[2] = Integer.parseInt(((Element) ((Element) role).getElementsByTagName("area").item(0)).getAttribute("h"));
		this.area[3] = Integer.parseInt(((Element) ((Element) role).getElementsByTagName("area").item(0)).getAttribute("w"));
		this.line = "\"" + ((Element) role).getElementsByTagName("line").item(0).getTextContent() + "\"";
	}
	
	/* Getters */
	public int getRank() {
		return this.rank;
	}
	public void setRank(int rank){
        this.rank = rank;
	}
	
	public void endRole() {
		this.actor.setCurrentRole(null);
		this.actor = null;
	}
	
	public boolean isWorked() {
		return this.actor != null;
	}
	public String getName() {
		return this.name;
	}
	
	public String getLine() {
		return this.line;
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
