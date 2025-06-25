package com.exam.project.builder;

import com.exam.project.factory.Character;
import com.exam.project.factoryMonster.AbstractMonster;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.iterator.Item;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Dungeon exploration system
 */
public class DungeonExplorer {

    private static final Logger logger = GameLogger.getLogger();
    private Scanner scanner;
    private Random random;
    private MonsterFactory monsterFactory;
    private CombatSystem combatSystem;

    public DungeonExplorer() {
        try {
            this.scanner = new Scanner(System.in);
            this.random = new Random();
            this.monsterFactory = new MonsterFactory();
            this.combatSystem = new CombatSystem();
            logger.info("DungeonExplorer initialized successfully");
        } catch (Exception e) {
            logger.severe("Failed to initialize DungeonExplorer: " + e.getMessage());
            throw new RuntimeException("Cannot create DungeonExplorer", e);
        }
    }

    /**
     * Explore a dungeon
     */
    public boolean exploreDungeon(Character character, Dungeon dungeon) {
        logger.info("Starting dungeon exploration");

        if (character == null || dungeon == null) {
            logger.warning("Exploration failed: null character or dungeon");
            System.out.println("Error: Missing character or dungeon!");
            return false;
        }

        if (!character.isAlive()) {
            logger.warning("Exploration failed: character is dead");
            System.out.println("Character is dead!");
            return false;
        }

        logger.info("Character " + character.getName() + " entering " + dungeon.getName());

        try {
            // Show dungeon info
            System.out.println("\n=== " + dungeon.getName().toUpperCase() + " ===");
            System.out.println(dungeon.getFullDescription());
            System.out.println("Your character: " + character);

            // Ask for confirmation
            System.out.print("\nEnter this dungeon? (y/n): ");
            String input = scanner.nextLine();

            if (!InputValidator.validateYesNo(input)) {
                logger.info("Player cancelled dungeon exploration");
                System.out.println("Maybe next time...");
                return false;
            }

            List<Item> loot = new ArrayList<>();

            // Explore each room
            for (int room = 1; room <= dungeon.getNumberOfRooms(); room++) {
                logger.info("Entering room " + room + " of " + dungeon.getNumberOfRooms());
                System.out.println("\n=== ROOM " + room + " ===");

                try {
                    // Create monsters for this room
                    List<AbstractMonster> monsters = createMonsters(dungeon);

                    if (monsters.isEmpty()) {
                        logger.info("Room " + room + " is empty");
                        System.out.println("This room is empty.");
                        continue;
                    }

                    // Show monsters
                    System.out.println("Enemies found:");
                    for (AbstractMonster monster : monsters) {
                        System.out.println("- " + monster);
                    }

                    // Fight!
                    if (!fight(character, monsters, loot)) {
                        logger.info("Character defeated in room " + room);
                        System.out.println("\nYou were defeated!");
                        return false;
                    }

                    // Rest between rooms
                    if (room < dungeon.getNumberOfRooms()) {
                        offerRest(character);
                    }

                } catch (Exception e) {
                    logger.severe("Error in room " + room + ": " + e.getMessage());
                    System.out.println("Something went wrong in this room! Continuing...");
                    continue;
                }
            }

            // Victory!
            logger.info("Dungeon completed successfully by " + character.getName());
            System.out.println("\n=== VICTORY! ===");
            System.out.println("You completed " + dungeon.getName() + "!");
            System.out.println("Gold earned: " + dungeon.getGoldReward());
            System.out.println("Experience: " + dungeon.getExperienceReward());

            // Give loot
            if (!loot.isEmpty()) {
                logger.info("Distributing " + loot.size() + " items to character");
                System.out.println("\nItems found:");
                for (Item item : loot) {
                    System.out.println("- " + item);
                    character.addItem(item);
                }
            }

            return true;

        } catch (Exception e) {
            logger.severe("Critical error during dungeon exploration: " + e.getMessage());
            System.out.println("A critical error occurred during exploration!");
            return false;
        }
    }

    /**
     * Create monsters for room
     */
    private List<AbstractMonster> createMonsters(Dungeon dungeon) {
        logger.info("Creating monsters for " + dungeon.getName());

        List<AbstractMonster> monsters = new ArrayList<>();

        try {
            String dungeonName = dungeon.getName().toLowerCase();
            String monsterType;
            int count = dungeon.getMonstersPerRoom();

            // Decide monster type based on dungeon
            if (dungeonName.contains("goblin")) {
                monsterType = "goblin";
                logger.info("Spawning goblins for Goblin Cave");
            } else if (dungeonName.contains("troll") || dungeonName.contains("swamp")) {
                monsterType = "troll";
                logger.info("Spawning trolls for Swamp of Trolls");
            } else {
                monsterType = "goblin"; // default
                logger.warning("Unknown dungeon type, defaulting to goblins");
            }

            // Sometimes add one more monster
            if (random.nextInt(100) < 30) {
                count++;
                logger.info("Extra monster spawned due to random chance");
            }

            // Create monsters
            for (int i = 0; i < count; i++) {
                try {
                    AbstractMonster monster = monsterFactory.createMonster(monsterType, dungeon.getBaseDifficulty());
                    if (monster != null) {
                        monsters.add(monster);
                        logger.info("Created " + monsterType + ": " + monster.getName());
                    } else {
                        logger.warning("Monster factory returned null for " + monsterType);
                    }
                } catch (Exception e) {
                    logger.warning("Failed to create individual monster: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.severe("Error creating monsters: " + e.getMessage());
        }

        logger.info("Created " + monsters.size() + " monsters for room");
        return monsters;
    }

    /**
     * Combat system
     */
    private boolean fight(Character character, List<AbstractMonster> monsters, List<Item> loot) {
        logger.info("Combat started: " + character.getName() + " vs " + monsters.size() + " monsters");
        System.out.println("\n--- COMBAT! ---");

        try {
            while (!monsters.isEmpty() && character.isAlive()) {
                // Show status
                System.out.println("\nYour HP: " + character.getHealth() + "/" + character.getMaxHealth());
                System.out.println("Your Stamina: " + character.getStamina() + "/" + character.getMaxStamina());
                System.out.println("Enemies left: " + monsters.size());

                // Player turn
                System.out.println("\n1. Attack");
                System.out.println("2. Try to flee");
                System.out.print("What do you do? ");

                String input;
                try {
                    input = scanner.nextLine();
                } catch (Exception e) {
                    logger.warning("Error reading player input: " + e.getMessage());
                    System.out.println("Input error! Defaulting to attack.");
                    input = "1";
                }

                Integer choice = InputValidator.validateMenuChoice(input, 2);

                if (choice == null) {
                    logger.info("Player entered invalid choice");
                    System.out.println("Invalid choice!");
                    continue;
                }

                if (choice == 1) {
                    // Attack
                    logger.info("Player chose to attack");

                    if (!combatSystem.canFight(character)) {
                        logger.info("Character too tired to fight");
                        System.out.println("You're too tired to fight!");
                        continue;
                    }

                    try {
                        AbstractMonster target = monsters.get(0);
                        int damage = combatSystem.executeAttack(character, target);

                        if (damage > 0) {
                            logger.info("Player attack successful: " + damage + " damage");
                            System.out.println("You attack for " + damage + " damage!");
                        } else {
                            logger.info("Player attack failed or missed");
                        }
                    } catch (Exception e) {
                        logger.severe("Error during player attack: " + e.getMessage());
                        System.out.println("Attack failed due to error!");
                    }

                } else if (choice == 2) {
                    // Try to flee
                    logger.info("Player attempting to flee");

                    try {
                        if (random.nextInt(100) < 50) {
                            logger.info("Flee attempt successful");
                            System.out.println("You escape!");
                            return false;
                        } else {
                            logger.info("Flee attempt failed");
                            System.out.println("Can't escape!");
                        }
                    } catch (Exception e) {
                        logger.warning("Error during flee attempt: " + e.getMessage());
                        System.out.println("Escape failed!");
                    }
                }

                // Remove dead monsters and collect loot
                try {
                    for (int i = monsters.size() - 1; i >= 0; i--) {
                        AbstractMonster monster = monsters.get(i);
                        if (monster != null && !monster.isAlive()) {
                            logger.info("Monster defeated: " + monster.getName());
                            System.out.println("You defeated " + monster.getName() + "!");

                            // Get loot
                            try {
                                List<Item> drops = monster.getDroppedItems();
                                for (Item item : drops) {
                                    System.out.println("Found: " + item);
                                    loot.add(item);
                                    logger.info("Loot collected: " + item.getName());
                                }
                            } catch (Exception e) {
                                logger.warning("Error collecting loot: " + e.getMessage());
                            }

                            monsters.remove(i);
                        }
                    }
                } catch (Exception e) {
                    logger.severe("Error processing defeated monsters: " + e.getMessage());
                }

                // Monster attacks
                if (!monsters.isEmpty() && character.isAlive()) {
                    logger.info("Monster turn: " + monsters.size() + " monsters attacking");
                    System.out.println("\n--- Monster Turn ---");

                    for (AbstractMonster monster : monsters) {
                        try {
                            if (monster != null && monster.isAlive() && character.isAlive()) {
                                int damage = combatSystem.executeMonsterAttack(monster, character);
                                if (damage > 0) {
                                    logger.info("Monster attack: " + monster.getName() + " deals " + damage + " damage");
                                    System.out.println(monster.getName() + " attacks you for " + damage + " damage!");
                                }
                            }
                        } catch (Exception e) {
                            logger.warning("Error during monster attack: " + e.getMessage());
                        }
                    }
                }
            }

            boolean victory = character.isAlive();
            logger.info("Combat ended. Player victory: " + victory);
            return victory;

        } catch (Exception e) {
            logger.severe("Critical error during combat: " + e.getMessage());
            System.out.println("Combat error occurred!");
            return false;
        }
    }

    /**
     * Offer rest between rooms
     */
    private void offerRest(Character character) {
        logger.info("Offering rest to character");

        try {
            System.out.print("\nRest to recover stamina? (y/n): ");
            String input = scanner.nextLine();

            if (InputValidator.validateYesNo(input)) {
                logger.info("Character chose to rest");

                int current = character.getStamina();
                int max = character.getMaxStamina();
                int restore = Math.min(20, max - current);

                if (restore > 0) {
                    character.restoreStamina(restore);
                    logger.info("Stamina restored: " + restore + " points");
                    System.out.println("Recovered " + restore + " stamina!");
                } else {
                    logger.info("Character already at full stamina");
                    System.out.println("Already at full stamina.");
                }
            } else {
                logger.info("Character declined rest");
            }
        } catch (Exception e) {
            logger.warning("Error during rest offer: " + e.getMessage());
            System.out.println("Rest attempt failed!");
        }
    }
}