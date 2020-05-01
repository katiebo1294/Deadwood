import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;
import java.util.Scanner;

public class Deadwood {

	private static int NUMBER_OF_DAYS = 4;
	public static EnumMap<RoomMap, Integer> roomMap = new EnumMap<RoomMap, Integer>(RoomMap.class);
	public static Room[] ROOMS;
	public static Board BOARD;
	public static Scene[] SCENES;

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
		setUpGame(players);
		int dayCount = 1;
		int playerCount;
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
		System.out.println("Goodbye.");
		scan.close();
	}

	private static void endDay(Player[] players, int dayCount) {
		// end all current roles and move players to Trailers
		for (Player p : players) {
			p.move(ROOMS[roomMap.get(RoomMap.TRAILERS)]);
			p.setCurrentRole(null);
		}
		// remove remaining scene card and deal the next 10 scene cards from the deck
		int deckIndex = (dayCount - 1) * 10;
		for (Room r : ROOMS) {
			r.setScene(null);
			r.setScene(SCENES[deckIndex]);
		}
	}

	/*
	 * Sets up the game at the beginning of playthrough -- creates all the rooms and
	 * scene cards and sets up the game board and players
	 */
	private static void setUpGame(Player[] players) {
		int numPlayers = players.length;
		System.out.println("Starting a new game with " + numPlayers + " players.");
		ROOMS = new Room[12];
		generateRooms(ROOMS);
		SCENES = new Scene[40];
		generateScenes(SCENES);
		shuffleDeck(SCENES);
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

	/* Sets up the game board at the start of each day -- TODO: might cut this since it could also just be implemented in main() */
	private static void playNextDay(int dayNum, Player[] players, Room[] rooms, Scene[] scenes) {
		Player p;
		String input;
		Scanner scan = new Scanner(System.in);
		// if the last day has already finished, proceed to end-of-game scoring instead
		if (dayNum == NUMBER_OF_DAYS + 1) {
			scoring(players);
		} else {
			// return players to their trailers and end any jobs they were working
			for (Player player : players) {
				player.move(rooms[roomMap.get(RoomMap.TRAILERS)]);
				player.setCurrentRole(null);
			}
			// remove the final scene card from the board and re-deal the next 10 scene
			// cards from the deck -- replace shot markers
			int deckIndex = 10 * (dayNum - 1);
			for (Room room : rooms) {
				if (!(room.getName().equals("Trailers") || room.getName().equals("Casting Office"))) {
					room.setScene(null);
					room.setScene(scenes[deckIndex]);
					deckIndex++;
					room.replaceShots();
				}
			}
		}
		// proceed to playing the day
		System.out.println("Starting day " + dayNum + "!");

		while (BOARD.getSceneCount() > 1) {
			for (int i = 0; i < players.length; i++) {
				p = players[i];
				System.out.println("Player " + (1 + i) + "'s turn.");
				// display player's info
				System.out.println("You have " + p.getNumCredits() + " credits, " + p.getNumDollars()
						+ " dollars, and you are rank " + p.getRank() + ".");
				Room currentRoom = p.getLocation();
				Scene currentScene = p.getLocation().getSceneCard();
				// if they are working a role, prompt them to rehearse or act
				if (p.getIsWorking()) {
					System.out.println("You are currently working on the role " + p.getCurrentRole() + " in the scene "
							+ currentScene.getSceneTitle() + ". Your location is " + currentRoom.getName() + ".");
					System.out.println("The scene's budget is " + currentScene.getBudget()
							+ " and your current number of practice chips is " + p.getNumPracticeChips() + ".");
					if ((p.getNumPracticeChips() + 1) >= currentScene.getBudget()) {
						System.out.println("You have reached a point of guaranteed success, so you must act.");
						actScene(p, currentRoom, currentScene);
					} else {
						System.out.println("Would you like to rehearse or act? ");
						input = scan.next();
						if (input.equalsIgnoreCase("rehearse".trim())) {
							p.rehearse();
							System.out
									.println("Good job! You now have " + p.getNumPracticeChips() + " practice chips.");
						} else if (input.equalsIgnoreCase("act".trim())) {
							actScene(p, currentRoom, currentScene);
						}
					}
				} else {
					// if they aren't working a role, prompt them to move or take a role (or both)
					System.out.println(
							"You are not currently working a role. Your location is " + currentRoom.getName() + ".");
					if (currentRoom.getName().equals("Trailers") || currentRoom.getName().equals("Casting Office")) {
						movePlayer(p, currentRoom);
					} else {
						System.out.println("Would you like to move or take a role?");
						input = scan.next();
						if (input.equalsIgnoreCase("move".trim())) {
							movePlayer(p, currentRoom);
						} else if (input.equalsIgnoreCase("take a role".trim())) {
							takeRole(p, currentRoom, currentScene);
						}
					}
				}
			}
		}
		scan.close();
		System.out.println("One active scene remaining! Ending the current day.");
		playNextDay(dayNum++, players, rooms, scenes);
	}

	private static void actScene(Player player, Room currentRoom, Scene currentScene) {
		int result;
		result = rollDie() + player.getNumPracticeChips();
		// act success
		if (result >= currentScene.getBudget()) {
			System.out.println("Nice job! You did it!");
			player.getLocation().removeShot();
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
						"You are now working " + r.getName() + " in the scene " + currentScene.getSceneTitle() + ".");
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
		for (Room room : currentRoom.getNeighbors()) {
			// match player's input to an available room and move them there
			if (input.equalsIgnoreCase(room.getName().trim())) {
				player.move(room);
				System.out.println("Moved to " + room.getName() + ".");
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
	private static void shuffleDeck(Scene[] scenes) {
		Random rand = new Random();
		int randomIndex;
		Scene temp;
		for (int i = 0; i < scenes.length; i++) {
			randomIndex = rand.nextInt(scenes.length);
			temp = scenes[randomIndex];
			scenes[randomIndex] = scenes[i];
			scenes[i] = temp;
		}

	}

	/*
	 * Generates 40 scene cards based on the Deadwood board game's original scenes
	 */
	private static void generateScenes(Scene[] scenes) {
		System.out.println("Generating 40 scene cards...");
		// create each scene
		ArrayList<Role> roles = new ArrayList<>();
		roles.add(new Role(2, "Defrocked Priest", "\"Look out below!\"", "Evil Wears a Hat"));
		roles.add(new Role(3, "Marshal Canfield", "\"Hold fast!\"", "Evil Wears a Hat"));
		roles.add(new Role(4, "One-Eyed Man", "\"Balderdash!\"", "Evil Wears a Hat"));
		scenes[0] = new Scene("Evil Wears a Hat",
				"Scene 7: Calhoun is separated from the group during a white-knuckle chase near Desperation Bluff.", 4,
				roles);
		roles.clear();
		roles.add(new Role(2, "Squeaking Boy", "\"I'll say!\"", "Square Deal City"));
		roles.add(new Role(4, "Pharaoh Imhotep", "\"Attack, soldiers!\"", "Square Deal City"));
		roles.add(new Role(6, "Aunt Martha", "\"You got nothin'!\"", "Square Deal City"));
		scenes[1] = new Scene("Square Deal City",
				"Scene 14: Douglas and Katherine confront Aunt Martha about her missing pies. Devin sulks quietly in a side room.",
				6, roles);
		roles.clear();
		roles.add(new Role(1, "Rug Merchant", "\"Don't leave my store!\"", "Law and the Old West"));
		roles.add(new Role(2, "Banker", "\"Trust me.\"", "Law and the Old West"));
		roles.add(new Role(5, "Talking Mule", "\"Nice work, Johnny!\"", "Law and the Old West"));
		scenes[2] = new Scene("Law and the Old West",
				"Scene 20: Charlie \"Three Guns\" Henderson cooperates with Johnny Law and reluctantly enters the witless protection program.",
				3, roles);
		roles.clear();
		roles.add(new Role(4, "The Duck", "\"Waaaack!\"", "Davy Crocket: A Drunkard's Tale"));
		roles.add(new Role(6, "His Brother", "\"Waaaaaaack!\"", "Davy Crockett: A Drunkard's Tale"));
		scenes[3] = new Scene("Davy Crocket: A Drunkard's Tale",
				"Scene 31: Robert enlists the aid of several farm animals in order to ascertain the efficacy of his new hangover remedy.",
				4, roles);
		roles.clear();
		roles.add(new Role(5, "Auctioneer", "\"Going once!\"", "The Life and Times of John Skywater"));
		roles.add(new Role(6, "General Custer", "\"Go West!\"", "The Life and Times of John Skywater"));
		scenes[4] = new Scene("The Life and Times of John Skywater",
				"Scene 22: Disheartened by his lack of business acumen and his poor choice of investment partners, John Skywater sets off into the Cree Nation to convince them to kidnap his wife.",
				5, roles);
		roles.clear();
		roles.add(new Role(2, "Town Drunk", "\"Even me!\"", "The Way the West Was Run"));
		roles.add(new Role(4, "Squinting Miner", "\"Sure we can!\"", "The Way the West Was Run"));
		roles.add(new Role(5, "Poltergeist", "\"Wooooo!\"", "The Way the West Was Run"));
		scenes[5] = new Scene("The Way the West Was Run",
				"Scene 34: Jose explains patiently, but with thinly veiled contempt, the intricacies of Arizona bureaucracy, as though speaking to a simple and distracted child.",
				4, roles);
		roles.clear();
		roles.add(new Role(3, "Drunk", "\"Where's Willard?\"", "My Years on the Prairie"));
		roles.add(new Role(4, "Librarian", "\"Shhhhh!\"", "My Years on the Prairie"));
		roles.add(new Role(5, "Man with Hay", "\"Hey!\"", "My Years on the Prairie"));
		scenes[6] = new Scene("My Years on the Prairie",
				"Scene 32: Virgil and Stacy set out at midnight to track down the stray cows, unaware that they are being pursued by inch-high aliens from outer space.",
				5, roles);
		roles.clear();
		roles.add(new Role(1, "Angry Barber", "\"Hold him still!\"", "Down in the Valley"));
		roles.add(new Role(3, "Woman with Board", "\"Nonsense, Frank!\"", "Down in the Valley"));
		roles.add(new Role(5, "Man on Fire", "\"It burns!\"", "Down in the Valley"));
		scenes[7] = new Scene("Down in the Valley",
				"Scene 24: A tripped waiter is the spark igniting a brawl of cataclysmic proportions. Walter is injured in the neck.",
				3, roles);
		roles.clear();
		roles.add(new Role(2, "Hollering Boy", "\"Over here, mister!\"", "Buffalo Bill: The Lost Years"));
		roles.add(new Role(3, "Drunk Farmer", "\"Git outta me barn!\"", "Buffalo Bill: The Lost Years"));
		roles.add(new Role(5, "Meek Little Sarah", "\"He's so cute!\"", "Buffalo Bill: The Lost Years"));
		scenes[8] = new Scene("Buffalo Bill: The Lost Years",
				"Scene 12: Buffalo Bill's companion Marty disappears in a freak electrical storm. Bill enlists the aid of the Sidekick Friends Network.",
				4, roles);
		roles.clear();
		roles.add(new Role(1, "Sleeping Man", "\"Snnkkk snnkkk snnkkk\"", "Ol' Shooter and Little Doll"));
		roles.add(new Role(2, "Man with Pig", "\"Tally-Hooo!\"", "Ol' Shooter and Little Doll"));
		roles.add(new Role(4, "Shooter", "\"Where's my britches?\"", "Ol' Shooter and Little Doll"));
		scenes[9] = new Scene("Ol' Shooter and Little Doll",
				"Scene 14: Shooter discovers that he has been proceeding for days with no trousers. This causes him no small embarrassment as he searches for them with Little Doll.",
				4, roles);
		roles.clear();
		roles.add(new Role(1, "Buster", "\"One two three go!\"", "The Robbers of Trains"));
		roles.add(new Role(4, "Man Reading Paper", "\"Ouchie!\"", "The Robbers of Trains"));
		roles.add(new Role(5, "Fat Pete", "\"Nice kick, boss!\"", "The Robbers of Trains"));
		scenes[10] = new Scene("The Robbers of Trains",
				"Scene 19: Coogan confronts the toughest thug in his gang, Big Jake, in an abbreviated knife fight. Coogan settles the dispute with fearless guile and a kick in the family jewels.",
				4, roles);
		roles.clear();
		roles.add(new Role(2, "Shot in Back", "\"Arrrggh!\"", "Jesse James: Man of Action"));
		roles.add(new Role(4, "Shot in Leg", "\"Ooh, lordy!\"", "Jesse James: Man of Action"));
		roles.add(new Role(5, "Leaps into Cake", "\"Dangit, Jesse!\"", "Jesse James: Man of Action"));
		scenes[11] = new Scene("Jesse James: Man of Action",
				"Scene 8: Jesse's brothers Jed and Henry throw him a surprise birthday party. Jesse's nerves get the better of him when the birthday cake explodes.",
				5, roles);
		roles.clear();
		roles.add(new Role(6, "Martin", "\"Have you tried soy cheese?\"", "Beyond the Pail: Life without Lactose"));
		scenes[12] = new Scene("Beyond the Pail: Life without Lactose",
				"Scene 12: Henry discovers for the first time that his ability to digest cream has disappeared along with his hair. Other cowboys attempt to console him.",
				2, roles);
		roles.clear();
		roles.add(new Role(2, "Piano Player", "\"It's a nocturne!\"", "Disaster at Flying J"));
		roles.add(new Role(3, "Man in Turban", "\"My stars!\"", "Disaster at Flying J"));
		roles.add(new Role(4, "Falls on Hoe", "\"Ow!\"", "Disaster at Flying J"));
		scenes[13] = new Scene("Disaster at Flying J",
				"Scene 6: After the mine explosion, the traveling circus takes time out to get drunk and start a fight.",
				5, roles);
		roles.clear();
		roles.add(new Role(3, "Preacher", "\"My word!\"", "A Man Called \"Cow\""));
		roles.add(new Role(6, "Amused Witness", "\"Tee hee hee!\"", "A Man Called \"Cow\""));
		scenes[14] = new Scene("A Man Called \"Cow\"",
				"Scene 16: Nothing will settle the debates among the skeptical locals, short of a demonstration of Hector's special talents.",
				3, roles);
		roles.clear();
		roles.add(new Role(1, "Falls from Tree", "\"What ho!\"", "Shakespeare in Lubbock"));
		roles.add(new Role(3, "Laughing Woman", "\"'Tis to laugh!\"", "Shakespeare in Lubbock"));
		roles.add(new Role(4, "Man with Whistle", "\"Tweeeeet!\"", "Shakespeare in Lubbock"));
		scenes[15] = new Scene("Shakespeare in Lubbock",
				"Scene 23: William decides that it is time to be movin' on. Julia convinces him to stick around just long enough to get into big trouble.",
				3, roles);
		roles.clear();
		roles.add(new Role(3, "Curious Girl", "\"Are you sure?\"", "Taffy Commercial"));
		roles.add(new Role(4, "Ghost of Plato", "\"It happened to me!\"", "Taffy Commercial"));
		scenes[16] = new Scene("Taffy Commercial",
				"Scene 2: Jackson encourages the children to eat only taffy, because gum can kill them stone dead.", 2,
				roles);
		roles.clear();
		roles.add(new Role(4, "Ex-Convict", "\"Never again!\"", "Go West, You!"));
		roles.add(new Role(6, "Man with Onion", "\"Fresh onions!\"", "Go West, You!"));
		scenes[17] = new Scene("Go West, You!",
				"Scene 30: Susan and Peter encounter some of the perils of the Badlands: rutted mud roads, torrential rain storms, and a bad case of \"grumble tummy.\"",
				3, roles);
		roles.clear();
		roles.add(new Role(2, "Surprised Bison", "\"Mmrrrrrph!\"", "Gum Commercial"));
		roles.add(new Role(4, "Man with Horn", "\"Ta daaaa!\"", "Gum Commercial"));
		scenes[18] = new Scene("Gum Commercial",
				"Scene 3: Inspector Pete speaks to a riveted audience about the many hidden dangers of taffy, not the least of which is that taffy can kill you stone dead.",
				2, roles);
		roles.clear();
		roles.add(new Role(3, "Staggering Man", "\"You never know!\"", "The Life and Times of John Skywater"));
		roles.add(new Role(5, "Woman with Beer", "\"Howdy, stranger!\"", "The Life and Times of John Skywater"));
		roles.add(new Role(6, "Marcie", "\"Welcome home!\"", "The Life and Times of John Skywater"));
		scenes[19] = new Scene("The Life and Times of John Skywater",
				"Scene 15: John discovers his long-lost sister Marcie, and instructs her in the ways of gunfighting and whiskey distillation.",
				5, roles);
		roles.clear();
		roles.add(new Role(4, "Looks like Elvis", "\"Thankyouverymuch.\"", "Gun! The Musical"));
		roles.add(new Role(5, "Singing Dead Man", "\"Yeah!\"", "Gun! The Musical"));
		roles.add(new Role(6, "Apothecary", "\"Such drugs I have.\"", "Gun! The Musical"));
		scenes[20] = new Scene("Gun! The Musical",
				"Scene 25: A song and dance extravaganza, \"Hunka Hunka Burnin' Lead.\"", 6, roles);
		roles.clear();
		roles.add(new Role(1, "Flustered Man", "\"Well, I never!\"", "One False Step for Mankind"));
		roles.add(new Role(2, "Space Monkey", "\"Ook!\"", "One False Step for Mankind"));
		roles.add(new Role(5, "Cowbot Dan", "\"Bzzzzzt!\"", "One False Step for Mankind"));
		scenes[21] = new Scene("One False Step for Mankind",
				"Scene 21: After a dozen failed attempts, one rocket carries Horatio and his six children to the Moon, where they enjoy a picnic and a spirited game of badminton.",
				6, roles);
		roles.clear();
		roles.add(new Role(2, "Jailer", "\"You there!\"", "Humor at the Expense of Others"));
		roles.add(new Role(4, "Mephistopheles", "\"Be not afraid!\"", "Humor at the Expense of Others"));
		roles.add(new Role(5, "Breaks a Window", "\"Oops!\"", "Humor at the Expense of Others"));
		scenes[22] = new Scene("Humor at the Expense of Others",
				"Scene 16: Phil and his cohort of unfeeling smart-mouths make fun of Sancho and his great big hat.", 5,
				roles);
		roles.clear();
		roles.add(new Role(1, "Man in Poncho", "\"Howdy, Jones!\"", "Thirteen the Hard Way"));
		roles.add(new Role(3, "Ecstatic Housewife", "\"This is fine!\"", "Thirteen the Hard Way"));
		roles.add(new Role(4, "Isaac", "\"The mail!\"", "Thirteen the Hard Way"));
		scenes[23] = new Scene("Thirteen the Hard Way",
				"Scene 15: After some delay, the Pony Express arrives. Isaac, Gwen, Francis, terry, Conrad, Brooke, Jerry, Joward, MacNeill, Jones, Spike, Cornwall and Crawford are all there.",
				5, roles);
		roles.clear();
		roles.add(new Role(5, "Film Critic", "\"Implausible!\"", "The Search for Maggie White"));
		roles.add(new Role(6, "Hobo with Bat", "\"Nice house!\"", "The Search for Maggie White"));
		scenes[24] = new Scene("The Search for Maggie White",
				"Scene 12: Alone in the wilderness, Maggie makes the best of her situation. In what seems like no time at all, she constructs a sturdy two-story house frmo branches and mud.",
				6, roles);
		roles.clear();
		roles.add(new Role(2, "Cow", "\"Moo.\"", "How They Get Milk"));
		roles.add(new Role(3, "St. Clement of Alexandria", "\"Peace be with you, child!\"", "How They Get Milk"));
		roles.add(new Role(4, "Josie", "\"Yikes!\"", "How They Get Milk"));
		scenes[25] = new Scene("How They Get Milk",
				"Scene 2:Josie asks the Milkman how they get milk. After a thoughtful pause, he begins. \"Not like you'd expect!\"",
				4, roles);
		roles.clear();
		roles.add(new Role(3, "Bewhisker'd Cowpoke", "\"Oh, sweet Lord!\"", "Picante Sauce Commercial"));
		roles.add(new Role(5, "Dog", "\"Wurf!\"", "Picante Sauce Commercial"));
		scenes[26] = new Scene("Picante Sauce Commercial",
				"Scene 1: A dozen grizzled cowboys surround a fire. Suddenly, they exclaim, \"That's not mayonnaise!\"",
				2, roles);
		roles.clear();
		roles.add(new Role(2, "Willard", "\"Aint' that a sight?\"", "My Years on the Prairie"));
		roles.add(new Role(3, "Leprechaun", "\"Begorrah!\"", "My Years on the Prairie"));
		roles.add(new Role(5, "Startled Ox", "\"Mrr?\"", "My Years on the Prairie"));
		scenes[27] = new Scene("My Years on the Prairie",
				"Scene 27: Louise takes instruction from Henry, the neighbor boy, in an absurdly suggestive explanation of how to plow a field.",
				5, roles);
		roles.clear();
		roles.add(new Role(1, "Shot in Head", "\"Arrrgh!\"", "Jesse James: Man of Action"));
		roles.add(new Role(4, "Leaps Out of Cake", "\"Oh, for Pete's sake!\"", "Jesse James: Man of Action"));
		roles.add(new Role(6, "Shot Three Times", "\"Ow! Ow! Ow!\"", "Jesse James: Man of Action"));
		scenes[28] = new Scene("Jesse James: Man of Action",
				"Scene 14: A hail of gunfire results when Jesse's friend Barton marries Jesse's childhood sweetheart.",
				5, roles);
		roles.clear();
		roles.add(new Role(2, "Voice of God", "\"Grab hold, son!\"", "Davy Crockett: A Drunkard's Tale"));
		roles.add(new Role(3, "Hands of God", "\"!\"", "Davy Crockett: A Drunkard's Tale"));
		roles.add(new Role(4, "Jack Kemp", "\"America!\"", "davy Crockett: A Drunkard's Tale"));
		scenes[29] = new Scene("Davy Crockett: A Drunkard's Tale",
				"Scene 12: In an absurd dream sequence, Crockett recalls an episode of fear and chaos in which his childhood friend Timmy was trapped at the bottom of a well.",
				4, roles);
		roles.clear();
		roles.add(new Role(5, "Opice (Monkey)", "\"Ukk! (Ook)!\"", "Czechs in the Sonora"));
		roles.add(new Role(6, "Man with Gun", "\"Hold it right there!\"", "Czechs in the Sonora"));
		scenes[30] = new Scene("Czechs in the Sonora",
				"Scene 25: Bob reverts to his ancestral ways in a short fight over a disembodied hand.", 4, roles);
		roles.clear();
		roles.add(new Role(1, "Man with Rope", "\"Look out below!\"", "J. Robert Lucky, Man of Substance"));
		roles.add(new Role(2, "Svetlana", "\"Says who?\"", "J. Robert Lucky, Man of Substance"));
		roles.add(new Role(5, "Accidental Victim", "\"Ow! My spine!\"", "J. Robert Lucky, Man of Substance"));
		scenes[31] = new Scene("J. Robert Lucky, Man of Substance",
				"Scene 13: Horace and Mathilde discover that the mysterious orange powder filling Doctor Lucky's air vents is neither Agent Orange nor weaponized tang, but a rare form of cheese mold.",
				4, roles);
		roles.clear();
		roles.add(new Role(1, "Thrifty Mike", "\"Call!\"", "Swing 'em Wide"));
		roles.add(new Role(3, "Sober Physician", "\"Raise!\"", "Swing 'em Wide"));
		roles.add(new Role(5, "Man on Floor", "\"Fold!\"", "Swing 'em Wide"));
		scenes[32] = new Scene("Swing 'em Wide",
				"Scene 19: Black Jack invites Dixon and The Captain to a late-night poker game. Little do they know that Gertrude and Isabella await them at the table.",
				6, roles);
		roles.clear();
		roles.add(new Role(2, "Very Wet Man", "\"Sheesh!\"", "Thirteen the Hard Way"));
		roles.add(new Role(4, "Dejected Housewife", "\"Its time had come.\"", "Thirteen the Hard Way"));
		roles.add(new Role(5, "Man with Box", "\"Progress!\"", "Thirteen the Hard Way"));
		scenes[33] = new Scene("Thirteen the Hard Way",
				"Scene 17: After operating for only six minutes, the Pony Express disbands and gives way to the international Telegraph and Railroad systems. Little boys cry.",
				5, roles);
		roles.clear();
		roles.add(new Role(3, "Liberated Nun", "\"Let me have it!\"", "Swing 'em Wide"));
		roles.add(new Role(5, "Witch Doctor", "\"Oogie Boogie!\"", "Swing 'em Wide"));
		roles.add(new Role(6, "Voice of Reason", "\"Come on, now!\"", "Swing 'em Wide"));
		scenes[34] = new Scene("Swing 'em Wide",
				"Scene 35: Hector makes a surprising discovery behind the Chinese grocery store.", 6, roles);
		roles.clear();
		roles.add(new Role(4, "Marksman", "\"Pull!\"", "How They Get Milk"));
		roles.add(new Role(5, "Postal Worker", "\"It's about time!\"", "How They Get Milk"));
		roles.add(new Role(6, "A Horse", "\"Yes Sir!\"", "How They Get Milk"));
		scenes[35] = new Scene("How They Get Milk",
				"Scene 8: Josie is thoroughly off milk at this point. The Milkman shows her one more way that she might not have heard before.",
				4, roles);
		roles.clear();
		roles.add(new Role(2, "Burning Man", "\"Make it stop!\"", "Trials of the First Pioneers"));
		roles.add(new Role(4, "Cheese Vendor", "\"Opa!\"", "Trials of the First Pioneers"));
		roles.add(new Role(5, "Hit with table", "\"Ow! A table?\"", "Trials of the First Pioneers"));
		scenes[36] = new Scene("Trials of the First Pioneers",
				"Scene 5: A fire breaks out in the town livery. Before long, the surrounding buildings are engulfed in flame. The world falls into chaos.",
				4, roles);
		roles.clear();
		roles.add(new Role(2, "Fraternity Pledge", "\"Beer me!\"", "Breakin' in Trick Ponies"));
		roles.add(new Role(6, "Man with Sword", "\"None shall pass!\"", "Breakin' in Trick Ponies"));
		scenes[37] = new Scene("Breakin' in Trick Ponies",
				"Scene 19: Uncle Stewart reveals what to do when all else fails.", 3, roles);
		roles.clear();
		roles.add(new Role(3, "Detective", "\"I have a hunch.\"", "How the Grinch Stole Texas"));
		roles.add(new Role(4, "File Clerk", "\"My stapler!\"", "How the Grinch Stole Texas"));
		roles.add(new Role(5, "Cindy Lou", "\"Dear Lord!\"", "How the Grinch Stole Texas"));
		scenes[38] = new Scene("How the Grinch Stole Texas",
				"Scene 9: the doe-eyed citizens of El Paso gather together around a warm fire and pray for the safety of those poor souls in Oklahoma. It almost works.",
				5, roles);
		roles.clear();
		roles.add(new Role(2, "Farmer", "\"Git off a that!\"", "Custer's Other Stands"));
		roles.add(new Role(4, "Exploding Horse", "\"Boom!\"", "Custer's Other Stands"));
		roles.add(new Role(6, "Jack", "\"Here we go again!\"", "Custer's Other Stands"));
		scenes[39] = new Scene("Custer's Other Stands",
				"Scene 40: General George Armstrong Custer clinches another victory at the battle of Little Sands. His trusty steed Cairo is not so lucky.",
				5, roles);
		roles.clear();
	}

	/*
	 * Generates 12 rooms based on the Deadwood board game's original rooms -
	 * assumes "basic"/default layout
	 */
	private static void generateRooms(Room[] rooms) {
		System.out.println("Generating 12 rooms in default configuration...");
		// initialize each room with name, roles and number of shots
		ArrayList<Role> roles = new ArrayList<>();
		Room trailers = new Room("Trailers", null, 0);
		Room castingOffice = new Room("Casting Office", null, 0);
		roles.add(new Role(1, "Railroad Worker", "\"I'm a steel-drivin' man!\"", "Main Street"));
		roles.add(new Role(2, "Falls off Roof", "\"Aaaaiiiigggghh!\"", "Main Street"));
		roles.add(new Role(2, "Woman in Black Dress", "\"Well, I'll be!\"", "Main Street"));
		roles.add(new Role(4, "Mayor McGinty", "\"People of Deadwood!\"", "Main Street"));
		Room mainStreet = new Room("Main Street", roles, 3);
		roles.clear();
		roles.add(new Role(1, "Reluctant Farmer", "\"I ain't so sure about that!\"", "Saloon"));
		roles.add(new Role(2, "Woman in Red Dress", "\"Come up and see me!\"", "Saloon"));
		Room saloon = new Room("Saloon", roles, 2);
		roles.clear();
		roles.add(new Role(2, "Prisoner in Cell", "\"Zzzzzzz... Whiskey!\"", "Jail"));
		roles.add(new Role(3, "Feller in Irons", "\"Ah kilt the wrong man!\"", "Jail"));
		Room jail = new Room("Jail", roles, 1);
		roles.clear();
		roles.add(new Role(1, "Crusty Prospector", "\"Aww, peaches!\"", "Train Station"));
		roles.add(new Role(1, "Dragged by Train", "\"Omgeezers!\"", "Train Station"));
		roles.add(new Role(2, "Preacher with Bag", "\"The Lord will provide.\"", "Train Station"));
		roles.add(new Role(4, "Cyrus the Gunfighter", "\"Git to fightin' or git away!\"", "Train Station"));
		Room trainStation = new Room("Train Station", roles, 3);
		roles.clear();
		roles.add(new Role(1, "Man in Overalls", "\"Looks like a storm's comin' in.\"", "General Store"));
		roles.add(new Role(3, "Mister Keach", "\"Howdy, stranger.\"", "General Store"));
		Room generalStore = new Room("General Store", roles, 2);
		roles.clear();
		roles.add(new Role(1, "Shot in Leg", "\"Ow! Me leg!\"", "Ranch"));
		roles.add(new Role(2, "Saucy Fred", "\"That's what she said!\"", "Ranch"));
		roles.add(new Role(3, "Man Under Horse", "\"A little help here!\"", "Ranch"));
		Room ranch = new Room("Ranch", roles, 3);
		roles.clear();
		roles.add(new Role(1, "Clumsy Pit Fighter", "\"Hit me!\"", "Secret Hideout"));
		roles.add(new Role(2, "Thug with Knife", "\"Meet Suzy, my murderin' knife.\"", "Secret Hideout"));
		roles.add(new Role(3, "Dangerous Tom", "\"There's two ways we can do this....\"", "Secret Hideout"));
		roles.add(new Role(4, "Penny, who is Lost", "\"Oh, woe! For I am lost!\"", "Secret Hideout"));
		Room secretHideout = new Room("Secret Hideout", roles, 3);
		roles.clear();
		roles.add(new Role(2, "Suspicious Gentleman", "\"Can you be more specific?\"", "Bank"));
		roles.add(new Role(3, "Flustered Teller", "\"Would you like a large bill, sir?\"", "Bank"));
		Room bank = new Room("Bank", roles, 1);
		roles.clear();
		roles.add(new Role(1, "Faro Player", "\"Hit me!\"", "Hotel"));
		roles.add(new Role(1, "Sleeping Drunkard", "\"Zzzzzz... Whiskey!\"", "Hotel"));
		roles.add(new Role(2, "Falls from Balcony", "\"Arrrgghh!!\"", "Hotel"));
		roles.add(new Role(3, "Australian Bartender", "\"What'll it be, mate?\"", "Hotel"));
		Room hotel = new Room("Hotel", roles, 3);
		roles.clear();
		roles.add(new Role(1, "Dead Man", "\"....\"", "Saloon"));
		roles.add(new Role(2, "Crying Woman", "\"Oh, the humanity!\"", "Saloon"));
		Room church = new Room("Church", roles, 2);
		// set adjacent rooms
		trailers.setNeighbor(mainStreet);
		trailers.setNeighbor(saloon);
		trailers.setNeighbor(hotel);
		castingOffice.setNeighbor(trainStation);
		castingOffice.setNeighbor(ranch);
		castingOffice.setNeighbor(secretHideout);
		mainStreet.setNeighbor(jail);
		mainStreet.setNeighbor(saloon);
		mainStreet.setNeighbor(trailers);
		saloon.setNeighbor(mainStreet);
		saloon.setNeighbor(trailers);
		saloon.setNeighbor(bank);
		saloon.setNeighbor(generalStore);
		jail.setNeighbor(trainStation);
		jail.setNeighbor(generalStore);
		jail.setNeighbor(mainStreet);
		trainStation.setNeighbor(jail);
		trainStation.setNeighbor(generalStore);
		trainStation.setNeighbor(castingOffice);
		generalStore.setNeighbor(trainStation);
		generalStore.setNeighbor(jail);
		generalStore.setNeighbor(saloon);
		generalStore.setNeighbor(ranch);
		ranch.setNeighbor(generalStore);
		ranch.setNeighbor(castingOffice);
		ranch.setNeighbor(bank);
		ranch.setNeighbor(secretHideout);
		secretHideout.setNeighbor(castingOffice);
		secretHideout.setNeighbor(church);
		secretHideout.setNeighbor(ranch);
		bank.setNeighbor(ranch);
		bank.setNeighbor(saloon);
		bank.setNeighbor(church);
		bank.setNeighbor(hotel);
		hotel.setNeighbor(trailers);
		hotel.setNeighbor(church);
		hotel.setNeighbor(bank);
		church.setNeighbor(hotel);
		church.setNeighbor(bank);
		church.setNeighbor(secretHideout);
		// add each room to the array
		rooms[0] = trainStation;
		rooms[1] = jail;
		rooms[2] = mainStreet;
		rooms[3] = generalStore;
		rooms[4] = saloon;
		rooms[5] = trailers;
		rooms[6] = castingOffice;
		rooms[7] = ranch;
		rooms[8] = bank;
		rooms[9] = secretHideout;
		rooms[10] = church;
		rooms[11] = hotel;
		// map each room's name to its location
		// create a board with these rooms
	}

}
