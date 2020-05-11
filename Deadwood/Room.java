import java.util.Arrays;

import org.w3c.dom.Element;

// TODO: maybe create subclasses for Set, Trailers, and Casting Office? 
public class Room {
	
	protected String name;
	protected String[] neighbors; //a list of adjacent rooms
	
	public Room(Element room) {
		this.neighbors = new String[((Element) room.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").getLength()];
		for(int i = 0; i < neighbors.length; i++) {
			String neighborName = ((Element) ((Element) room.getElementsByTagName("neighbors").item(0)).getElementsByTagName("neighbor").item(i)).getAttribute("name");
			if(neighborName.equals("office")) {
				neighbors[i] = "Casting Office";
			} else if(neighborName.equals("trailer")) {
				neighbors[i] = "Trailers";
			} else {
				neighbors[i] = neighborName;
			}
		}
	}

	/* Getters */
	public String[] getNeighbors() {
		return this.neighbors;
	}
	
	public String getName() {
		return this.name;
	}
	
	// Lists the name of each adjacent room in this.neighbors in a comma-separated list
	public String listNeighbors() {
		return Arrays.toString(this.neighbors);
	}
	
	public String toString() {
		return "name = '" + this.name + "'";
	}
}
