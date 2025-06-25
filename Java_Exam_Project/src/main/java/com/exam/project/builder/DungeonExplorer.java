package com.exam.project.builder;

import com.exam.project.factory.Character;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.iterator.Item;
import com.exam.project.security.InputValidator;
import com.exam.project.security.ExceptionHandler;
import com.exam.project.strategy.CombatSystem;
import com.exam.project.logger.GameLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Manages dungeon exploration with comprehensive crash protection
 */
public class DungeonExplorer {

    private static final Logger logger = GameLogger.getLogger();
    private final Scanner scanner;
    private final Random random;
    private final MonsterFactory monsterFactory;
    private final CombatSystem combatSystem;

    /**
     * Creates DungeonExplorer with protected initialization
     */
    public DungeonExplorer() {
        try {
            this.scanner = new Scanner(System.in);
            this.random = new Random();
            this.monsterFactory = new MonsterFactory();
            this.combatSystem = new CombatSystem();
            logger.info("DungeonExplorer initialized successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DungeonExplorer", e);
        }
    }

    /**
     * Starts dungeon exploration with full error protection
     * @param character The player's character
     * @param dungeon The dungeon to explore
     * @return true if dungeon completed, false otherwise
     */
    public boolean exploreDungeon(Character character, Dungeon dungeon) {
        try {
            // Input validation
            if (character == null) {
                System.out.println("Error: No character provided for exploration!");
                return false;
            }

            if (dungeon == null) {
                System.out.println("Error: No dungeon provided for exploration!");
                return false;
            }

            if (!character.isAlive()) {
                System.out.println("A defeated character cannot explore dungeons!");
                return false;
            }

            // Display dungeon info safely
            displayDungeonInfo(character, dungeon);

            // Get user confirmation with input validation
            if (!getPlayerConfirmation()) {
                System.out.println("You decide to prepare more before entering...");
                return false;
            }

            // Initialize tracking variables
            int totalGold = 0;
            List<Item> collectedItems = new ArrayList<>();

            // Explore each room with error handling
            for (int room = 1; room <= dungeon.getNumberOfRooms(); room++) {
                try {
                    System.out.println("\n=== ROOM " + room + " of " + dungeon.getNumberOfRooms() + " ===");

                    List<AbstractMonster> monsters = generateMonstersSafely(dungeon);

                    if (monsters.isEmpty()) {
                        System.out.println("This room is empty.");
                        continue;
                    }

                    displayMonsters(monsters);

                    if (!handleCombatSafely(character, monsters, collectedItems)) {
                        System.out.println("\nYou were defeated in room " + room + "!");
                        return false;
                    }

                    // Offer rest between rooms (but not after final room)
                    if (room < dungeon.getNumberOfRooms()) {
                        offerRestSafely(character);
                    }

                } catch (Exception e) {
                    ExceptionHandler.handleException(e, "Error in room " + room);
                    System.out.println("Something went wrong in this room. Moving to next room...");
                    continue;
                }
            }

            // Handle successful completion
            handleVictorySafely(character, dungeon, totalGold, collectedItems);
            return true;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Critical error during dungeon exploration");
            return false;
        }
    }

    /**
     * Displays dungeon information safely
     * @param character The player character
     * @param dungeon The dungeon to display
     */
    private void displayDungeonInfo(Character character, Dungeon dungeon) {
        try {
            System.out.println("\n=== ENTERING " + dungeon.getName().toUpperCase() + " ===");
            System.out.println(dungeon.getFullDescription());
            System.out.println("\nYour character: " + character);
        } catch (Exception e) {
            System.out.println("Error displaying dungeon info.");
            logger.warning("Error in displayDungeonInfo: " + e.getMessage());
        }
    }

    /**
     * Gets player confirmation with input validation
     * @return true if player confirms, false otherwise
     */
    private boolean getPlayerConfirmation() {
        try {
            System.out.print("\nEnter this dungeon? (y/n): ");
            String input = scanner.nextLine();
            return InputValidator.validateYesNo(input);
        } catch (Exception e) {
            logger.warning("Error getting player confirmation: " + e.getMessage());
            return false; // Safe default - don't enter dungeon on error
        }
    }

    /**
     * Generates monsters with comprehensive error handling
     * @param dungeon The current dungeon
     * @return List of monsters (empty list if error)
     */
    private List<AbstractMonster> generateMonstersSafely(Dungeon dungeon) {
        List<AbstractMonster> monsters = new ArrayList<>();

        try {
            int count = dungeon.getMonstersPerRoom();

            // Add variation safely
            if (random.nextInt(100) < 30) {
                count += 1;
            }

            // Ensure count is within reasonable bounds
            count = Math.max(1, Math.min(4, count));

            List<String> types = dungeon.getMonsterTypes();
            if (types == null || types.isEmpty()) {
                logger.warning("Dungeon has no monster types defined");
                return monsters; // Return empty list
            }

            for (int i = 0; i < count; i++) {
                try {
                    String type = types.get(random.nextInt(types.size()));
                    AbstractMonster monster = monsterFactory.createMonster(type, dungeon.getBaseDifficulty());

                    if (monster != null) {
                        monsters.add(monster);
                    } else {
                        logger.warning("Monster creation returned null for type: " + type);
                    }
                } catch (Exception e) {
                    logger.warning("Error creating individual monster: " + e.getMessage());
                    // Continue with other monsters
                }
            }

        } catch (Exception e) {
            logger.severe("Error generating monsters: " + e.getMessage());
            // Return whatever monsters we managed to create
        }

        return monsters;
    }

    /**
     * Displays monsters safely
     * @param monsters List of monsters to display
     */
    private void displayMonsters(List<AbstractMonster> monsters) {
        try {
            System.out.println("Enemies found:");
            for (AbstractMonster monster : monsters) {
                if (monster != null) {
                    System.out.println("- " + monster);
                }
            }
        } catch (Exception e) {
            System.out.println("Error displaying monster information.");
            logger.warning("Error in displayMonsters: " + e.getMessage());
        }
    }

    /**
     * Handles combat with comprehensive error protection
     * @param character The player character
     * @param monsters List of enemy monsters
     * @param collectedItems Items collected during combat
     * @return true if character survives, false otherwise
     */
    private boolean handleCombatSafely(Character character, List<AbstractMonster> monsters, List<Item> collectedItems) {
        try {
            System.out.println("\n--- COMBAT BEGINS ---");

            while (!monsters.isEmpty() && character.isAlive()) {
                // Display status safely
                displayCombatStatus(character, monsters);

                // Get player action with validation
                Integer choice = getPlayerAction();
                if (choice == null) {
                    System.out.println("Invalid input! You lose your turn!");
                    continue;
                }

                // Handle player action
                if (choice == 1) {
                    handlePlayerAttackSafely(character, monsters);
                } else if (choice == 2) {
                    if (attemptFlee()) {
                        System.out.println("You escaped from combat!");
                        return false;
                    }
                }

                // Collect loot safely
                collectLootSafely(monsters, collectedItems);

                // Monster attacks
                if (!monsters.isEmpty() && character.isAlive()) {
                    handleMonsterAttacksSafely(character, monsters);
                }
            }

            return character.isAlive();

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Error during combat");
            return false; // Assume defeat on critical error
        }
    }

    /**
     * Displays combat status safely
     * @param character The player character
     * @param monsters List of monsters
     */
    private void displayCombatStatus(Character character, List<AbstractMonster> monsters) {
        try {
            System.out.println("\nYour status: " + character);
            System.out.println("Enemies remaining: " + monsters.size());
        } catch (Exception e) {
            System.out.println("Error displaying combat status.");
        }
    }

    /**
     * Gets player action with input validation
     * @return Player choice or null if invalid
     */
    private Integer getPlayerAction() {
        try {
            System.out.println("\n1. Attack");
            System.out.println("2. Try to flee");
            System.out.print("Action: ");
            String input = scanner.nextLine();
            return InputValidator.validateMenuChoice(input, 2);
        } catch (Exception e) {
            logger.warning("Error getting player action: " + e.getMessage());
            return null;
        }
    }

    /**
     * Handles player attack with error protection
     * @param character The attacking character
     * @param monsters List of target monsters
     */
    private void handlePlayerAttackSafely(Character character, List<AbstractMonster> monsters) {
        try {
            if (monsters.isEmpty()) {
                System.out.println("No enemies to attack!");
                return;
            }

            AbstractMonster target = monsters.get(0);
            if (target != null) {
                combatSystem.executeAttack(character, target);
            }
        } catch (Exception e) {
            System.out.println("Attack failed due to error.");
            logger.warning("Error in player attack: " + e.getMessage());
        }
    }

    /**
     * Attempts to flee from combat
     * @return true if escape successful, false otherwise
     */
    private boolean attemptFlee() {
        try {
            if (random.nextInt(100) < 50) {
                return true;
            } else {
                System.out.println("Escape failed!");
                return false;
            }
        } catch (Exception e) {
            logger.warning("Error in flee attempt: " + e.getMessage());
            return false; // Fail to escape on error
        }
    }

    /**
     * Handles monster attacks safely
     * @param character The target character
     * @param monsters List of attacking monsters
     */
    private void handleMonsterAttacksSafely(Character character, List<AbstractMonster> monsters) {
        try {
            System.out.println("\n--- MONSTER ATTACKS ---");
            for (AbstractMonster monster : monsters) {
                if (monster != null && monster.isAlive() && character.isAlive()) {
                    combatSystem.executeMonsterAttack(monster, character);
                }
            }
        } catch (Exception e) {
            System.out.println("Error during monster attacks.");
            logger.warning("Error in monster attacks: " + e.getMessage());
        }
    }

    /**
     * Collects loot safely using iterator to prevent ConcurrentModificationException
     * @param monsters List of monsters to check
     * @param collectedItems List to add items to
     */
    private void collectLootSafely(List<AbstractMonster> monsters, List<Item> collectedItems) {
        try {
            Iterator<AbstractMonster> iterator = monsters.iterator();
            while (iterator.hasNext()) {
                AbstractMonster monster = iterator.next();

                if (monster != null && !monster.isAlive()) {
                    try {
                        int gold = monster.getGoldDrop();
                        List<Item> items = monster.getDroppedItems();

                        System.out.println("Gained " + gold + " gold from " + monster.getName() + "!");

                        if (items != null && !items.isEmpty()) {
                            System.out.println("Items found:");
                            for (Item item : items) {
                                if (item != null) {
                                    System.out.println("- " + item);
                                    collectedItems.add(item);
                                }
                            }
                        }

                        iterator.remove(); // Safe removal during iteration

                    } catch (Exception e) {
                        logger.warning("Error collecting loot from monster: " + e.getMessage());
                        iterator.remove(); // Remove problematic monster
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Critical error in loot collection: " + e.getMessage());
        }
    }

    /**
     * Offers rest option safely
     * @param character The character to rest
     */
    private void offerRestSafely(Character character) {
        try {
            System.out.print("\nRest to recover stamina? (y/n): ");
            String input = scanner.nextLine();

            if (InputValidator.validateYesNo(input)) {
                int current = character.getStamina();
                int max = character.getMaxStamina();
                int recovered = Math.min(20, max - current);

                if (recovered > 0) {
                    character.restoreStamina(recovered);
                    System.out.println("Recovered " + recovered + " stamina.");
                } else {
                    System.out.println("You are already at full stamina.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error during rest attempt.");
            logger.warning("Error in rest: " + e.getMessage());
        }
    }

    /**
     * Handles victory safely
     * @param character The victorious character
     * @param dungeon The completed dungeon
     * @param totalGold Gold earned
     * @param collectedItems Items collected
     */
    private void handleVictorySafely(Character character, Dungeon dungeon, int totalGold, List<Item> collectedItems) {
        try {
            System.out.println("\n=== DUNGEON COMPLETED! ===");
            System.out.println("You conquered " + dungeon.getName() + "!");

            int finalGold = totalGold + dungeon.getGoldReward();
            System.out.println("Total gold: " + finalGold);
            System.out.println("Experience: " + dungeon.getExperienceReward());

            if (collectedItems != null && !collectedItems.isEmpty()) {
                System.out.println("\nItems collected:");
                for (Item item : collectedItems) {
                    if (item != null) {
                        System.out.println("- " + item);
                        character.addItem(item);
                    }
                }
            }

            System.out.println("\nFinal status: " + character);
            logger.info("Dungeon completed: " + dungeon.getName() + " by " + character.getName());

        } catch (Exception e) {
            System.out.println("Error handling victory, but you still won!");
            logger.warning("Error in victory handling: " + e.getMessage());
        }
    }
}