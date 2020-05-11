
public class Player {

	private int numCredits;
	private int numDollars;
	private int rank;
	private boolean isWorking; // if they are currently on a role
	private Role currentRole;
	private Room location;
	private int numPracticeChips;
	
	/* Used for modifications to gameplay due to having more or less than 4 players */
	public Player(int numCredits, int rank) {
		this.numCredits = numCredits;
		this.numDollars = 0;
		this.rank = rank;
		this.isWorking = false;
		this.numPracticeChips = 0;
		this.location = Board.lookUpRoom("Trailers");
	}
	
	/* Standard setup for 4 players */
	public Player() {
		this.numCredits = 0;
		this.numDollars = 0;
		this.rank = 1;
		this.isWorking = false;
		this.location = Board.lookUpRoom("Trailers");
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
	
	public Room getLocation() {
		return this.location;
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
	public int upgradeRank(int rank) {
		int success = 0;
		if(rank == 2) {
			if(this.numCredits >= 5 || this.numDollars >= 4) {
				success = 1;
				this.rank = 2;
			}
		} else if(rank == 3) {
			if(this.numCredits >= 10 || this.numDollars >= 10) {
				success = 1;
				this.rank = 3;
			}
		} else if(rank == 4) {
			if(this.numCredits >= 15 || this.numDollars >= 18) {
				success = 1;
				this.rank = 4;
			}
		} else if(rank == 5) {
			if(this.numCredits >= 20 || this.numDollars >= 28) {
				success = 1;
				this.rank = 5;
			}
		} else if(rank == 6) {
			if(this.numCredits >= 25 || this.numDollars >= 40) {
				success = 1;
				this.rank = 6;
			}
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
	
	public int move(Room room) {
		int success = 0;
		for(String neighbor : this.location.getNeighbors()) {
			if(Board.lookUpRoom(neighbor) == room) {
				this.location = room;
				success = 1;
			}
		}
		return success;
	}
	
	public void rehearse() {
		this.numPracticeChips++;
	}
	
}