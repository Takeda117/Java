package com.exam.project.builder;

import com.exam.project.combat.CombatSystem;
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
 * Implementa il Builder Pattern per costruire l'esperienza di esplorazione
 */
public class DungeonExplorer {

    private static final Logger logger = GameLogger.getLogger();
    private Scanner scanner;
    private Random random;
    private MonsterFactory monsterFactory;
    private CombatSystem combatSystem;
    
    // Builder per l'esplorazione
    private Character character;
    private Dungeon dungeon;
    private boolean enableRest = true;
    private boolean enableFlee = true;
    private int difficultyModifier = 0;

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
    
    // Builder methods
    public DungeonExplorer withCharacter(Character character) {
        this.character = character;
        return this;
    }
    
    public DungeonExplorer withDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
        return this;
    }
    
    public DungeonExplorer enableRest(boolean enable) {
        this.enableRest = enable;
        return this;
    }
    
    public DungeonExplorer enableFlee(boolean enable) {
        this.enableFlee = enable;
        return this;
    }
    
    public DungeonExplorer withDifficultyModifier(int modifier) {
        this.difficultyModifier = modifier;
        return this;
    }
    
    public boolean build() {
        logger.info("Building dungeon exploration experience");
        
        if (character == null || dungeon == null) {
            logger.warning("Cannot build exploration: missing character or dungeon");
            return false;
        }
        
        return exploreDungeon(character, dungeon);
    }

    /**
     * Explore a dungeon
     */
    public boolean exploreDungeon(Character character, Dungeon dungeon) {
        logger.info("Starting dungeon exploration");

        if (character == null || dungeon == null || !character.isAlive()) {
            logger.warning("Exploration failed: invalid character or dungeon");
            System.out.println("Error: Cannot start exploration!");
            return false;
        }

        logger.info("Character " + character.getName() + " entering " + dungeon.getName());

        try {
            // Show dungeon info and confirm entry
            System.out.println("\n=== " + dungeon.getName().toUpperCase() + " ===");
            System.out.println(dungeon.getFullDescription());
            System.out.println("Your character: " + character);

            System.out.print("\nEnter this dungeon? (y/n): ");
            String confirmInput = scanner.nextLine();
            if (!InputValidator.validateYesNo(confirmInput)) {
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
                    List<AbstractMonster> monsters = dungeon.createMonstersForRoom();
                    
                    if (monsters == null || monsters.isEmpty()) {
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
                    if (enableRest && room < dungeon.getNumberOfRooms()) {
                        offerRest(character);
                    }
                } catch (Exception e) {
                    logger.severe("Error in room " + room + ": " + e.getMessage());
                    System.out.println("Something went wrong in this room! Continuing...");
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
                if (enableFlee) System.out.println("2. Try to flee");
                System.out.print("What do you do? ");

                String input;
                try {
                    input = scanner.nextLine();
                    logger.fine("Combat input received: " + input);
                } catch (Exception e) {
                    logger.warning("Error reading combat input: " + e.getMessage() + ". Using default '1'");
                    input = "1";
                }

                Integer choice = InputValidator.validateMenuChoice(input, enableFlee ? 2 : 1);

                if (choice == null) {
                    logger.warning("Invalid combat choice: " + input);
                    System.out.println("Invalid choice!");
                    continue;
                }

                if (choice == 1) {
                    // Attack
                    if (!combatSystem.canFight(character)) {
                        logger.info("Character too tired to fight: " + character.getName());
                        System.out.println("You're too tired to fight!");
                        continue;
                    }

                    AbstractMonster target = monsters.get(0);
                    logger.info("Character attacking: " + character.getName() + " -> " + target.getType() + " " + target.getName());
                    int damage = combatSystem.executeAttack(character, target);

                    if (damage > 0) {
                        logger.info("Player attack successful: " + damage + " damage");
                    }
                } else if (choice == 2 && enableFlee) {
                    // Try to flee
                    logger.info("Character attempting to flee: " + character.getName());
                    if (random.nextInt(100) < 50) {
                        logger.info("Flee attempt successful");
                        System.out.println("You escape!");
                        return false;
                    } else {
                        logger.info("Flee attempt failed");
                        System.out.println("Can't escape!");
                    }
                }

                // Remove dead monsters and collect loot
                for (int i = monsters.size() - 1; i >= 0; i--) {
                    AbstractMonster monster = monsters.get(i);
                    if (monster != null && !monster.isAlive()) {
                        logger.info("Monster defeated: " + monster.getName());
                        
                        // Get loot
                        List<Item> drops = monster.getDroppedItems();
                        if (drops != null) {
                            for (Item item : drops) {
                                loot.add(item);
                                logger.info("Loot collected: " + item.getName());
                            }
                        }
                        
                        monsters.remove(i);
                    }
                }

                // Monster attacks
                if (!monsters.isEmpty() && character.isAlive()) {
                    logger.info("Monster turn: " + monsters.size() + " monsters attacking");
                    System.out.println("\n--- Monster Turn ---");

                    for (AbstractMonster monster : monsters) {
                        if (monster != null && monster.isAlive() && character.isAlive()) {
                            combatSystem.executeMonsterAttack(monster, character);
                        }
                    }
                }
            }

            boolean result = character.isAlive();
            logger.info("Combat ended. Character survived: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Critical error during combat: " + e.getMessage(), e);
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
            if (InputValidator.validateYesNo(scanner.nextLine())) {
                int current = character.getStamina();
                int max = character.getMaxStamina();
                int restore = Math.min(20, max - current);

                if (restore > 0) {
                    character.restoreStamina(restore);
                    logger.info("Stamina restored: " + restore + " points");
                    System.out.println("Recovered " + restore + " stamina!");
                } else {
                    System.out.println("Already at full stamina.");
                }
            }
        } catch (Exception e) {
            logger.warning("Error during rest offer: " + e.getMessage());
            System.out.println("Rest attempt failed!");
        }
    }
}