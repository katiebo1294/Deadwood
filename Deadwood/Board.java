public  class Board {

	private Room[] rooms;
	
	public Board(Room[] rooms) {
		this.rooms = rooms;
	}
	
	/* Getters */
	public Room[] getRooms() {
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
