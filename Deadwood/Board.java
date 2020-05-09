import java.util.ArrayList;

public  class Board {

	private ArrayList<Room> rooms;
	
	public Board(ArrayList<Room> ROOMS) {
		this.rooms = ROOMS;
	}
	
	/* Getters */
	public ArrayList<Room> getRooms() {
		return this.rooms;
	}
	
	// counts how many scenes are still on the board at any given point in the game
	public int getSceneCount() {
		int sceneCount = 0;
		for(Room room : this.rooms) {
			if(room.getSceneCard() != null) {
				sceneCount++;
			}
		}
		return sceneCount;
	}
}
