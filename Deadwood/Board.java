import org.w3c.dom.Element;

public  class Board {

	private static String name;
	private static Room[] rooms;
	
	public Board(Element board) {
		name = board.getAttribute("name");
		rooms = new Room[board.getElementsByTagName("set").getLength() + 2];
		for(int i = 0; i < rooms.length - 2; i++) {
			rooms[i] = new Set((Element) board.getElementsByTagName("set").item(i));
		}
		rooms[rooms.length-2] = new Trailers((Element) board.getElementsByTagName("trailer").item(0));
		rooms[rooms.length-1] = new CastingOffice((Element) board.getElementsByTagName("office").item(0));
		
	}

	/* Getters */
	
	public static String getName() {
		return name;
	}
	
	public static Room[] getRooms() {
		return rooms;
	}
	
	public static Room lookUpRoom(String roomName) {
		for(Room room : rooms) {
			if(room.getName().equalsIgnoreCase(roomName)) {
				return room;
			}
		}
		return null;
	}
	
	// Counts how many scenes are still on the board at any given point in the game
	public int getSceneCount() {
		int sceneCount = 0;
		for(Room room : rooms) {
			if((room instanceof Set) && ((Set) room).getSceneCard() != null) {
				sceneCount++;
			}
		}
		return sceneCount;
	}
}
