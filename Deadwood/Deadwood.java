import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Deadwood {

	public static int NUMBER_OF_DAYS = 4;
	public static Board BOARD;
	public static Scene[] SCENES;


	public static void main(String[] args) throws ParserConfigurationException {

		Scanner scan = new Scanner(System.in);
		String input = "";
		int numPlayers = 0;
		System.out.println("Welcome to Deadwood!");
		System.out.println("How many players?");
		do {
			System.out.print("Please enter a number between 2 and 8. ");
			while (!scan.hasNextInt()) {
				input = scan.next();
				System.out.print("Please enter a valid number. ");
			}
			numPlayers = scan.nextInt();
		} while (numPlayers < 2 || numPlayers > 8);

		Player[] players = new Player[numPlayers];
		setUpGame(players);
		int dayCount = 1;
		int playerCount = 1;
		boolean endTurn = false;
		boolean endGame = false;
		// Still in progress ---
		// loop through whole game
		while (dayCount < NUMBER_OF_DAYS && endGame == false) {
			// loop through each day
			System.out.println("Start of day " + dayCount + ".");
			while (BOARD.getSceneCount() > 1) {
				// loop through each player's turn;
				endTurn = false;
				do {
					playerCount = 1;
					while (playerCount <= players.length) {
						System.out.println("Player " + playerCount + "'s turn.");
						Player currentPlayer = players[playerCount - 1];
						do {
							Scene currentScene;
							Room currentRoom = currentPlayer.getLocation();
							if(currentRoom instanceof Set) {
								currentScene = ((Set)currentRoom).getSceneCard();
							} else {
								currentScene = null;
							}
							
							
							System.out.println("What would you like to do?");
							System.out.print("> ");
							input = scan.next();
							// display player's information
							if (input.equalsIgnoreCase("info")) {
								System.out.println("Number of credits: " + currentPlayer.getNumCredits());
								System.out.println("Number of dollars: " + currentPlayer.getNumDollars());
								System.out.println("Current rank: " + currentPlayer.getRank());
								System.out.print("Current Role: ");
								if (currentPlayer.getIsWorking()) {
									System.out.println(currentPlayer.getCurrentRole().getName() + " in scene "
											+ currentScene.getTitle());
								} else {
									System.out.println("none");
								}
								// display location of all players and who is currently active
							} else if (input.equalsIgnoreCase("location")) {
								System.out.println("You are in " + currentRoom.getName());
								for (int i = 0; i < players.length; i++) {
									if (i != playerCount) {
										System.out.println("Player " + (i + 1) + " is in " + currentRoom.getName());
									}
								}
								// displays available rooms and moves the player to the selected room
							} else if(input.equalsIgnoreCase("end")) {
								endTurn = true;
							} else if(input.equalsIgnoreCase("quit")) {
								endGame = true;
							} else if (input.equalsIgnoreCase("move")) {
								if(!currentPlayer.getIsWorking()) {
								
									String [] neighborStrings = currentRoom.getNeighbors();
									Room [] neighbors = new Room[neighborStrings.length];
									
									//convert strings into rooms
									for(int i = 0; i < neighborStrings.length; i++) {
										neighbors[i] = Board.lookUpRoom(neighborStrings[i]);
									}
		
									System.out.println("Available rooms: " + currentRoom.listNeighbors());
									System.out.print("> ");
									input = scan.next();		//Should be nextLine for main street
									boolean match = false;
									
									for (Room neighbor : neighbors) {
											if (input.equalsIgnoreCase(neighbor.getName().trim())) {
												currentPlayer.move(neighbor);
												currentRoom = currentPlayer.getLocation();
												currentScene = ((Set)currentRoom).getSceneCard();
												match = true;		
												boolean askRole = false;
												
												System.out.println("You have moved to " + currentRoom.getName() + ".");
												
												if(currentRoom instanceof Set) {
													while(askRole == false) {
														// prompt them to take a role at the new location
														String response;
														System.out.print("Would you like to take a role? ('yes' or 'no'): ");
														response = scan.next();
														if (response.equalsIgnoreCase("yes".trim())) {
															takeRole(currentPlayer, ((Set) currentRoom), currentScene);
															askRole = true;
														} else if(response.equalsIgnoreCase("no".trim())) {
															System.out.println("no role taken");
															askRole = true;
														} 
													}
												}
												endTurn = true;
												break;
											}
									} 
									if(!match) {
										System.out.println("That is not a place you can move to. Please try again.");
									}
								} else {
									System.out.println("You cannot move while working a role");
								} 
							} else if (input.equalsIgnoreCase("rehearse")) {
								if (currentPlayer.getIsWorking()) {
									if(currentScene.getBudget() + currentPlayer.getNumPracticeChips() < 6){
										currentPlayer.rehearse();
										System.out.println("You now have " + currentPlayer.getNumPracticeChips() + " rehearsal chips.");
										endTurn = true;
									} else {
										System.out.println("You are guarenteed to succeed. Go Act!");
									}
								} else {
									System.out.println("You must be working a role in order to rehearse.");
								}
							} else if (input.equalsIgnoreCase("act")) {
								if (currentPlayer.getIsWorking()) {
									actScene(currentPlayer, ((Set)currentRoom), currentScene);
									break;
								} else {
									System.out.println("You must be working a role in order to act.");
								}
							} else if (input.equalsIgnoreCase("upgrade")) {
								if (currentRoom.getName().equals("Casting Office")) {
									boolean chooseUpgrade = false;
									int rankNum = 0;
									
									System.out.println(CastingOffice.displayPriceList());
									System.out.println();
									System.out.println("Current Rank: " + currentPlayer.getRank());
									System.out.println("You have: " + currentPlayer.getNumDollars() + " Dollars");
									System.out.println("          " + currentPlayer.getNumCredits() + " Credits");
									
									while(chooseUpgrade == false){
									
										while (rankNum < 2 || rankNum > 6){
											System.out.println("What rank would you like to upgrade to? (If you no longer want to upgrade, enter your current rank)");
											System.out.print("Desired Rank: ");
											while(!scan.hasNextInt()){
												System.out.println("Please enter a valid rank (2-6)");
												rankNum = scan.nextInt();
											}
											rankNum = scan.nextInt();
										} 
										
										if(rankNum < currentPlayer.getRank()){					
											System.out.println("Sorry, you cannot downgrade");
											rankNum = 0;
										} else if(rankNum == currentPlayer.getRank()){
										
											System.out.println("No upgrade");
											chooseUpgrade = true;
											
										} else if(rankNum > currentPlayer.getRank()){
										
											boolean canUpgrade = upgradePlayerRank(rankNum, currentPlayer);
											if(canUpgrade){
												//Player has upgraded
												chooseUpgrade = true;
											} else{
												//Player cannot upgrade to desired rank, reprompted
												System.out.println("You cannot upgrade to that rank");
												rankNum = 0;
											}
										}
									}

									break;
								} else {
									System.out.println(
											"You must be in the Casting Office to upgrade your rank. You are in "
													+ currentRoom.getName() + ".");
								}
							} else if (input.equalsIgnoreCase("take role")) {
								String listOfRoles = currentScene.listAvailableRoles(currentPlayer.getRank());
								if (currentPlayer.getIsWorking()) {
									System.out.println("You are already working "
											+ currentPlayer.getCurrentRole().getName() + ".");
								} else if(listOfRoles == ""){
									System.out.println("Sorry, there are no roles to take here.");
								} else {
									takeRole(currentPlayer, ((Set) currentRoom), currentScene);
									endTurn = true;
									break;
								}
							} else {
								
								String listOfRoles = "";
								if(currentPlayer.getLocation().getName() != "Casting OFfice" &&
															currentPlayer.getLocation().getName() != "Trailers") {
									listOfRoles = currentScene.listAvailableRoles(currentPlayer.getRank());
								} else {
									 	System.out.println("Please enter a valid command");
									 	System.out.println("You are able to:");
									 	if(currentPlayer.getIsWorking()){
									 		if(currentScene.getBudget() + currentPlayer.getNumPracticeChips() < 6){
									 			System.out.println("-> Rehearse");
									 		} 
									 		System.out.println("-> Act");
									 	} else{
									 		System.out.println("-> Move");
									 		if(listOfRoles != ""){
									 			System.out.println("-> Take Role");
									 		}
									 		if(currentPlayer.getRank() != 6 && (currentRoom.getName().equals("Casting Office"))){
									 			System.out.println("-> Upgrade");
									 		}
									 	}
								}
							}
						} while (!endTurn); 
						playerCount++;
					}
				} while (!endGame); 
			}
			System.out.println("End of day " + dayCount + ".");
			endDay(players, dayCount);
			dayCount++;
		}
		System.out.println("The game is ending.");
		scoring(players);
		System.out.println("Goodbye."); //*/
		System.out.println("(rest of the game is still under construction)\nGoodbye.");
		scan.close();
	}

	private static void endDay(Player[] players, int dayCount) {
		// end all current roles and move players to Trailers
		for (Player p : players) {
			p.move(Board.lookUpRoom("Trailers"));
			p.setCurrentRole(null);
		}
		// remove remaining scene card and deal the next 10 scene cards from the deck
		int deckIndex = (dayCount - 1) * 10;
		for (Room r : Board.getRooms()) {
			if(r instanceof Set) {
				((Set) r).setScene(null);
				((Set) r).setScene(SCENES[deckIndex]);
			}
		}
	}

	/*
	 * Sets up the game at the beginning of playthrough -- creates all the rooms and
	 * scene cards and sets up the game board and players
	 */
	private static void setUpGame(Player[] players) throws ParserConfigurationException {
		int numPlayers = players.length;
		System.out.println("Starting a new game with " + numPlayers + " players.");
		parseBoard();
		SCENES = parseCards();
		shuffleDeck();
		generatePlayers(players);
		endDay(players, 1); /*
		for(Room r : Board.getRooms()) {
			System.out.println(r.toString());
		}
		for(Scene s : SCENES) {
			System.out.println(s.toString());
		} */
		System.out.println(CastingOffice.displayPriceList());
	}

	/*
	 * Creates the player objects -- if the number of players is not 4, takes care
	 * of modifications to starting amounts/rank
	 */
	private static void generatePlayers(Player[] players) {
		for (int i = 0; i < players.length; i++) {
			// if 2 or 3 players, only play 3 days instead of 4
			if (players.length < 4) {
				NUMBER_OF_DAYS = 3;
				players[i] = new Player();
				// if 4 players, make no changes (standard playthrough)
			} else if (players.length == 4) {
				players[i] = new Player();
				// if 5 players, start everyone with 2 credits
			} else if (players.length == 5) {
				players[i] = new Player(2, 1);
				// if 6 players, start everyone with 4 credits
			} else if (players.length == 6) {
				players[i] = new Player(4, 1);
				// if 7 or 8 players, start everyone at rank 2 instead
			} else {
				players[i] = new Player(0, 2);
			}
		}
		System.out.println("Number of days to play is " + NUMBER_OF_DAYS + ".");
		System.out.println("Generating players with rank " + players[0].getRank() + " and " + players[0].getNumCredits()
				+ " extra starting credits...");
	}

	
	private static void actScene(Player player, Set currentRoom, Scene currentScene) {
		int result;
		result = rollDie() + player.getNumPracticeChips();
		// act success
		if (result >= currentScene.getBudget()) {
			System.out.println("Nice job! You did it!");
			currentRoom.removeShot();
			// if they are working a role off-the-card, they get 1 dollar and 1 credit
			for(Role role : currentRoom.getRoles()) {
				if(player.getCurrentRole() == role) {
					player.addCredits(1);
					player.addDollars(1);
				// if they are working a role on-the-card, they get 2 credits
				} else {
				player.addCredits(2);
				}
			}
			// if the last shot marker was removed, proceed to payout
			if (currentRoom.getRemainingShots() == 0) {
				if (currentScene.actorsOnCard()) {
					System.out.println("That's a wrap!");
					payout(currentRoom, currentScene);
					currentRoom.removeSceneCard();
					player.setCurrentRole(null);
				}
			}
			// act fail
		} else {
			System.out.println("Oops! You failed to perform the scene. Try again next round.");
			// if they are working on a role off-the-card, they receive 1 dollar (otherwise
			// no reward)
			for(Role role : currentRoom.getRoles()) {
				if(player.getCurrentRole() == role) {
					player.addDollars(1);
				}
			}
		}
	}
	//sets for off the card, not on card.
	private static void takeRole(Player player, Set currentRoom, Scene currentScene) {
		String input;
		Scanner scan = new Scanner(System.in);
		boolean availableRole = false;
		// list available roles, separated by role type (extra or starring)

		
		String onCardRoles = currentScene.listAvailableRoles(player.getRank());
		String offCardRoles = currentRoom.listAvailableRoles(player.getRank());
		if(onCardRoles == "" && offCardRoles == "") {
			System.out.println("Sorry, there are no roles to take");
			availableRole = true;
		}
/*2
		System.out.println("The available off-the-card roles are: " + offCardRoles);
		System.out.println("The available on-the-card roles are: " + onCardRoles);
		System.out.print("Desired Role (Case Sensitive for now): ");
		input = scan.nextLine();
		
*/
		
		while(availableRole == false) {
			
			System.out.println("The available off-the-card roles are: " + offCardRoles);
			System.out.println("The available on-the-card roles are: " + onCardRoles);
			System.out.print("Desired Role: ");
			input = scan.nextLine();
			
			if(offCardRoles.toUpperCase().contains(input.toUpperCase())) {
				for(Role offCard : currentRoom.getRoles()) {
					if (input.equalsIgnoreCase(offCard.getName().trim())) {
						player.setCurrentRole(offCard);
						availableRole = true;
						System.out.println(
								"You are now working " + offCard.getName() + " in the scene " + currentScene.getTitle() + ".");
					}
				}
				
			} else if(onCardRoles.toUpperCase().contains(input.toUpperCase())) {
				for(Role onCard : currentScene.getRoles()) {
					if (input.equalsIgnoreCase(onCard.getName().trim())) {
						player.setCurrentRole(onCard);
						availableRole = true;
						System.out.println(
								"You are now working " + onCard.getName() + " in the scene " + currentScene.getTitle() + ".");
					}
				}
			} else {
				System.out.println("Please enter a valid role.");
			}
		}
	//	scan.close();	keep scanner open for next players turn
	}

	private static void movePlayer(Player player, Room currentRoom) {
		String input;
		Scanner scan = new Scanner(System.in);
		// list the rooms adjacent to player's current location
		System.out.println("The available rooms are: " + currentRoom.listNeighbors() + ".");
		System.out.print("Where would you like to go? ");
		input = scan.next();
		for (String room : currentRoom.getNeighbors()) {
			// match player's input to an available room and move them there
			if (input.trim().equalsIgnoreCase(room)) {
				player.move(Board.lookUpRoom(room));
				System.out.println("Moved to " + room + ".");
				currentRoom = player.getLocation();
			}
		}
		if(currentRoom instanceof Set) {
			// prompt them to take a role at the new location
			System.out.print("Would you like to take a role? ");
			input = scan.next();
			if (input.equalsIgnoreCase("yes".trim())) {
				takeRole(player, ((Set) currentRoom), ((Set) currentRoom).getSceneCard());
			}
		}
		scan.close();
	}

	/*
	 * Payout at the end of a scene - assumes there was at least one actor working
	 * on-the-card
	 */
	private static void payout(Set room, Scene scene) {
		// sort the on-card ranks from highest to lowest
		Role[] orderedRanks = scene.getRoles();
		Arrays.sort(orderedRanks);
		// roll the die as many times as the budget ($4 million budget = 4 die rolls)
		int numRolls = scene.getBudget();
		int result = 0;
		// distribute that roll's amount in dollars to each rank on the card, highest to lowest
		for (int i = 0; i < numRolls; i++) {
			for (Role star : orderedRanks) {
				result = rollDie();
				if (star.getIsWorked()) {
					star.getActor().addDollars(result);
				}
				i++;
			}
		}
		// extras all get the dollar amount equivalent to their role's rank
		for (Role extra : room.getRoles()) {
			if (extra.getIsWorked()) {
				extra.getActor().addDollars(extra.getRank());
			}
		}
	}

	/* Provides a random integer between 1 and 6 */
	private static int rollDie() {
		return (int) ((Math.random() * 6) + 1);
	}
	
	/*Returns whether or not Player is allowed to upgrade to desired rank; upgrades if true*/
	private static boolean upgradePlayerRank(int desired, Player currentPlayer) {
		int cash = currentPlayer.getNumDollars();
		int cred = currentPlayer.getNumCredits();
		String payment = "";
		Scanner scan = new Scanner(System.in);
		boolean CoD = false;
		
		while(CoD == false) {
			System.out.print("What would you like to use as payment for the upgrade? (enter 'dollars' or 'credits'): ");
			payment = scan.next();
			if(payment.equalsIgnoreCase("dollars") || payment.equalsIgnoreCase("credits")) {
				CoD = true;
			} else {
				System.out.println("Please enter valid payment type ('dollars' or 'credits')");
			}
		}
					
			if(desired == 2) {
				if(cash >= 4 && payment.equalsIgnoreCase("dollars")) {
					currentPlayer.upgradeRank(2);
					currentPlayer.modifyDollars(4);
					return true;
				} else if(cred >= 5 && payment.equalsIgnoreCase("credits")) {
					currentPlayer.upgradeRank(2);
					currentPlayer.modifyCredits(5);
					return true;
				} else {
					return false;
				}				
			} else if(desired == 3) {				
				if(cash >= 10 && payment.equalsIgnoreCase("dollars")) {
					currentPlayer.upgradeRank(3);
					currentPlayer.modifyDollars(10);
					return true;
				} else if(cred >= 10 && payment.equalsIgnoreCase("credits")) {
					currentPlayer.upgradeRank(3);
					currentPlayer.modifyCredits(10);
					return true;
				} else {
					return false;
				}				
			} else if(desired == 4) {				
				if(cash >= 18 && payment.equalsIgnoreCase("dollars")) {
					currentPlayer.upgradeRank(4);
					currentPlayer.modifyDollars(18);
					return true;
				} else if(cred >= 15 && payment.equalsIgnoreCase("credits")) {
					currentPlayer.upgradeRank(4);
					currentPlayer.modifyCredits(15);
					return true;
				} else {
					return false;
				}				
			} else if(desired == 5) {				
				if(cash >= 28 && payment.equalsIgnoreCase("dollars")) {
					currentPlayer.upgradeRank(5);
					currentPlayer.modifyDollars(28);
					return true;
				} else if(cred >= 20 && payment.equalsIgnoreCase("credits")) {
					currentPlayer.upgradeRank(5);
					currentPlayer.modifyCredits(20);
					return true;
				} else {
					return false;
				}				
			} else if(desired == 6) {				
				if(cash >= 40 && payment.equalsIgnoreCase("dollars")) {
					currentPlayer.upgradeRank(6);
					currentPlayer.modifyDollars(40);
					return true;
				} else if(cred >= 25 && payment.equalsIgnoreCase("credits")) {
					currentPlayer.upgradeRank(6);
					currentPlayer.modifyCredits(25);
					return true;
				} else {
					return false;
				}
			}			
			return false;		
		}
	

	/*
	 * For scoring at the end of the game: credits + dollars + five times your rank
	 */
	private static void scoring(Player[] players) {
		int score = 0;
		int max = 0;
		int winnerIndex = 0;
		// calculate and display each player's score
		for (int i = 0; i < players.length; i++) {
			score = players[i].getNumCredits() + players[i].getNumDollars() + (players[i].getRank() * 5);
			if (score > max) {
				max = score;
				winnerIndex = i;
			}
			System.out.println("Player " + (i + 1) + "'s score: " + score);
		}
		// announce winner (highest score)
		System.out.println("The winner is: " + "Player " + (winnerIndex + 1) + "!");
	}

	/* Shuffles the deck of scene cards */
	private static void shuffleDeck() {
		Random rand = new Random();
		int randomIndex;
		Scene temp;
		for (int i = 0; i < SCENES.length; i++) {
			randomIndex = rand.nextInt(SCENES.length);
			temp = SCENES[randomIndex];
			SCENES[randomIndex] = SCENES[i];
			SCENES[i] = temp;
		}

	}
	
	private static void parseBoard() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document board = null;
		
		try {
			board = builder.parse("board.xml");
			BOARD = new Board((Element) board.getElementsByTagName("board").item(0));
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
	}
	
	private static Scene[] parseCards() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document cards;
		Scene[] scenes;
		
		try {
			cards = builder.parse("cards.xml");
			scenes = new Scene[cards.getElementsByTagName("card").getLength()];
			for(int i = 0; i < scenes.length; i++) {
				if(cards.getElementsByTagName("card").item(i).getNodeType() == Node.ELEMENT_NODE) {
					scenes[i] = new Scene((Element) cards.getElementsByTagName("card").item(i));
				}
			}
			return scenes;
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
		return null;
	}
}
