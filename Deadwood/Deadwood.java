import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Deadwood {

	private static int NUMBER_OF_DAYS = 4;
	public static EnumMap<RoomMap, Integer> roomMap = new EnumMap<RoomMap, Integer>(RoomMap.class);
	public static ArrayList<Room> ROOMS;
	public static Board BOARD;
	public static ArrayList<Scene> SCENES;

	public static void main(String[] args) {
		// to map each room name to its index in the storage array
		roomMap.put(RoomMap.TRAINSTATION, 0);
		roomMap.put(RoomMap.JAIL, 1);
		roomMap.put(RoomMap.MAINSTREET, 2);
		roomMap.put(RoomMap.GENERALSTORE, 3);
		roomMap.put(RoomMap.SALOON, 4);
		roomMap.put(RoomMap.TRAILERS, 5);
		roomMap.put(RoomMap.CASTINGOFFICE, 6);
		roomMap.put(RoomMap.RANCH, 7);
		roomMap.put(RoomMap.BANK, 8);
		roomMap.put(RoomMap.SECRETHIDEOUT, 9);
		roomMap.put(RoomMap.CHURCH, 10);
		roomMap.put(RoomMap.HOTEL, 11);

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
		//setUpGame(players);
		int dayCount = 1;
		int playerCount;
		/* Still in progress ---
		// loop through whole game
		while (dayCount < NUMBER_OF_DAYS) {
			// loop through each day
			System.out.println("Start of day " + dayCount + ".");
			while (BOARD.getSceneCount() > 1) {
				// loop through each player's turn
				playerCount = 1;
				do {
					while (playerCount <= players.length) {
						System.out.println("Player " + playerCount + "'s turn.");
						Player currentPlayer = players[playerCount];
						do {
							Room currentRoom = currentPlayer.getLocation();
							Scene currentScene = currentRoom.getSceneCard();
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
											+ currentScene.getSceneTitle());
								} else {
									System.out.println("none");
								}
								// display location of all players and who is currently active
							} else if (input.equalsIgnoreCase("location")) {
								System.out.println("You are in " + currentRoom.getName());
								for (int i = 0; i < players.length; i++) {
									if (i != playerCount) {
										System.out.println("Player " + (i + 1) + "is in " + currentRoom.getName());
									}
								}
								// displays available rooms and moves the player to the selected room
							} else if (input.equalsIgnoreCase("move")) {
								ArrayList<Room> neighbors = currentRoom.getNeighbors();
								System.out.println("Available rooms: " + currentRoom.listNeighbors());
								System.out.print("> ");
								input = scan.next();
								boolean match = false;
									for (Room neighbor : neighbors) {
										if (input.equalsIgnoreCase(neighbor.getName().trim())) {
											currentPlayer.move(neighbor);
											match = true;
											break;
										}
								}
								if(!match) {
									System.out.println("That is not a place you can move to.");
								}
							} else if (input.equalsIgnoreCase("rehearse")) {
								if (currentPlayer.getIsWorking()) {
									// rehearse
									// give related info
									break;
								} else {
									System.out.println("You must be working a role in order to rehearse.");
								}
							} else if (input.equalsIgnoreCase("act")) {
								if (currentPlayer.getIsWorking()) {
									// act
									// give related info
									break;
								} else {
									System.out.println("You must be working a role in order to act.");
								}
							} else if (input.equalsIgnoreCase("upgrade")) {
								if (currentRoom.getName().equals("Casting Office")) {
									// upgrade role (if they can afford it)
									// show current and target rank
									break;
								} else {
									System.out.println(
											"You must be in the Casting Office to upgrade your rank. You are in "
													+ currentRoom.getName() + ".");
								}
							} else if (input.equalsIgnoreCase("take role")) {
								if (currentPlayer.getIsWorking()) {
									System.out.println("You are already working "
											+ currentPlayer.getCurrentRole().getName() + ".");
								} else {
									// show available roles (only within their rank)
									// set player's current role to the one they choose
									break;
								}
							} else {
								// error msg
								// give command options (only ones that apply specifically to this player during
								// this turn)
							}
						} while (!input.equalsIgnoreCase("end".trim()));
						playerCount++;
					}
				} while (!input.equalsIgnoreCase("quit".trim()));
			}
			System.out.println("End of day " + dayCount + ".");
			endDay(players, dayCount);
			dayCount++;
		}
		System.out.println("The game is ending.");
		scoring(players);
		System.out.println("Goodbye."); */
		System.out.println("(rest of the game is still under construction)\nGoodbye.");
		scan.close();
	}

	private static void endDay(Player[] players, int dayCount) {
		// end all current roles and move players to Trailers
		for (Player p : players) {
			p.move(ROOMS.get(11).getName());
			p.setCurrentRole(null);
		}
		// remove remaining scene card and deal the next 10 scene cards from the deck
		int deckIndex = (dayCount - 1) * 10;
		for (Room r : ROOMS) {
			r.setScene(null);
			r.setScene(SCENES.get(deckIndex));
		}
	}

	/*
	 * Sets up the game at the beginning of playthrough -- creates all the rooms and
	 * scene cards and sets up the game board and players
	 */
	private static void setUpGame(Player[] players) throws ParserConfigurationException {
		int numPlayers = players.length;
		System.out.println("Starting a new game with " + numPlayers + " players.");
		ROOMS = parseBoard();
		SCENES = parseCards();
		shuffleDeck();
		BOARD = new Board(ROOMS);
		generatePlayers(players);
		endDay(players, 1);
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

	
	private static void actScene(Player player, Room currentRoom, Scene currentScene) {
		int result;
		result = rollDie() + player.getNumPracticeChips();
		// act success
		if (result >= currentScene.getBudget()) {
			System.out.println("Nice job! You did it!");
			//player.getLocation().removeShot();
			// if they are working a role off-the-card, they get 1 dollar and 1 credit
			if (currentRoom.getRoles().contains(player.getCurrentRole())) {
				player.addDollars(1);
				player.addCredits(1);
				// if they are working a role on-the-card, they get 2 credits
			} else {
				player.addCredits(2);
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
			if (currentRoom.getRoles().contains(player.getCurrentRole())) {
				player.addDollars(1);
			}
		}
	}

	private static void takeRole(Player player, Room currentRoom, Scene currentScene) {
		String input;
		Scanner scan = new Scanner(System.in);
		// list available roles, separated by role type (extra or starring)
		System.out.println("The available off-the-card roles are: " + currentRoom.listRoles(player.getRank()));
		System.out.println("The available on-the-card roles are: " + currentScene.listRoles(player.getRank()));
		input = scan.next();
		for (Role r : currentRoom.getRoles()) {
			// match player's input to an available role and assign them to that role
			if (input.equalsIgnoreCase(r.getName().trim())) {
				player.setCurrentRole(r);
				System.out.println(
						"You are now working " + r.getName() + " in the scene " + currentScene.getTitle() + ".");
			}
		}
		scan.close();
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
			if (input.equalsIgnoreCase(room.trim())) {
				player.move(room);
				System.out.println("Moved to " + room + ".");
			}
		}
		// prompt them to take a role at the new location
		System.out.print("Would you like to take a role? ");
		input = scan.next();
		if (input.equalsIgnoreCase("yes".trim())) {
			takeRole(player, currentRoom, currentRoom.getSceneCard());
		}
		scan.close();
	}

	/*
	 * Payout at the end of a scene - assumes there was at least one actor working
	 * on-the-card
	 */
	private static void payout(Room room, Scene scene) {
		// sort the on-card ranks from highest to lowest
		ArrayList<Role> orderedRanks = scene.getRoles();
		Collections.sort(orderedRanks);
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
		for (int i = 0; i < SCENES.size(); i++) {
			randomIndex = rand.nextInt(SCENES.size());
			temp = SCENES.get(randomIndex);
			SCENES.set(randomIndex, SCENES.get(i));
			SCENES.set(i, temp);
		}

	}
	
	private static ArrayList<Room> parseBoard() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document sets = null;
		ArrayList<Room> rooms = new ArrayList<>();
		
		try {
			sets = builder.parse("board.xml");
			NodeList setList = sets.getElementsByTagName("set");
			for(int i = 0; i < setList.getLength(); i++) {
				Node s = setList.item(i);
				if(s.getNodeType() == Node.ELEMENT_NODE) {
					rooms.add(new Room(s));
				}
			}
			rooms.add(new Room(sets.getElementsByTagName("trailer").item(0), ((Element) sets.getElementsByTagName("trailer").item(0)).getAttribute("name")));
			rooms.add(new Room(sets.getElementsByTagName("office").item(0)));
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
		return rooms;
	}
	
	private static ArrayList<Scene> parseCards() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document cards = null;
		ArrayList<Scene> scenes = new ArrayList<>();
		
		try {
			cards = builder.parse("cards.xml");
			NodeList cardList = cards.getElementsByTagName("card");
			for(int i = 0; i < cardList.getLength(); i++) {
				Node c = cardList.item(i);
				if(c.getNodeType() == Node.ELEMENT_NODE) {
					scenes.add(new Scene(c));
				}
			}
		} catch (Exception ex) {
			System.out.println("XML parse failure");
			ex.printStackTrace();
		}
		return scenes;
	}
}
