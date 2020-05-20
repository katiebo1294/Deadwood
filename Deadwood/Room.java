import java.util.Arrays;

import org.w3c.dom.Element;

public class Room {
	
	protected String name;
	protected String[] neighbors; //a list of adjacent rooms
	protected int[] area; //x,y,h,w
	
	
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
		this.area = new int[4];
		this.area[0] = Integer.parseInt(((Element) room.getElementsByTagName("area").item(0)).getAttribute("x"));
		this.area[1] = Integer.parseInt(((Element) room.getElementsByTagName("area").item(0)).getAttribute("y"));
		this.area[2] = Integer.parseInt(((Element) room.getElementsByTagName("area").item(0)).getAttribute("h"));
		this.area[3] = Integer.parseInt(((Element) room.getElementsByTagName("area").item(0)).getAttribute("w"));
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
		String result = Arrays.toString(this.neighbors);
		return result.substring(1, result.length()-1);
	}
}
