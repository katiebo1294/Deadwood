
public class Player {

	private int numCredits;
	private int numDollars;
	private int rank;
	private boolean isWorking; // if they are currently on a role
	private Role currentRole;
	private Room room;
	private int numPracticeChips;
	
	/* Used for modifications to gameplay due to having more or less than 4 players */
	public Player(int numCredits, int rank) {
		this.numCredits = numCredits;
		this.numDollars = 0;
		this.rank = rank;
		this.isWorking = false;
		this.numPracticeChips = 0;
		this.room = Board.lookUpRoom("Trailers");
	}
	
	/* Standard setup for 4 players */
	public Player() {
		this.numCredits = 0;
		this.numDollars = 0;
		this.rank = 1;
		this.isWorking = false;
		this.room = Board.lookUpRoom("Trailers");
	}
	
	/* Getters */
	public int getNumCredits() {
		return this.numCredits;
	}
	
	public int getNumDollars() {
		return this.numDollars;
	}
	
	public int getRank() {
		return rank;
	}
	
	public boolean getIsWorking() {
		return this.isWorking;
	}
	
	public Role getCurrentRole() {
		return this.currentRole;
	}
	
	public Room getRoom() {
		return this.room;
	}
	
	public int getNumPracticeChips() {
		return this.numPracticeChips;
	}
	
	/* Setters */
	public void addCredits(int credits) {
		this.numCredits += credits;
	}
	
	public void addDollars(int dollars) {
		this.numDollars += dollars;
	}
	
	// based on the original Deadwood boardgame's price list for upgrading rank
	public boolean upgradeRank(int rank) {
		boolean success = false;
		int[][]  priceList = CastingOffice.getPriceList();
		if(this.numDollars >= priceList[rank-2][0] || this.numCredits >= priceList[rank-2][1]) {
			this.rank = rank;
			success = true;
		}
		return success;
	}
	
	public void setCurrentRole(Role role) {
		if(role != null) {
			this.isWorking = true;
			role.setActor(this);
		} else {
			this.isWorking = false;
			this.numPracticeChips = 0;
		}
		this.currentRole = role;
	}
	
	public boolean move(Room room) {
		boolean success = false;
		for(String neighbor : this.room.getNeighbors()) {
			if(Board.lookUpRoom(neighbor) == room) {
				this.room = room;
				success = true;
			}
		}
		return success;
	}
	
	public void rehearse() {
		this.numPracticeChips++;
	}
	
	public void modifyDollars(int dollars) {
		this.numDollars = this.numDollars - dollars;
	}
	
	public void modifyCredits(int credits) {
		this.numCredits = this.numCredits - credits;
	}
	
	
}
