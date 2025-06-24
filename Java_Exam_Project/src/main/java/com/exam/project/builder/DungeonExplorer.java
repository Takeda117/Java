package com.exam.project.builder;

import com.exam.project.factory.Character;
import com.exam.project.factoryMonster.Monster;
import com.exam.project.factoryMonster.MonsterFactory;
import com.exam.project.iterator.Item;
import com.exam.project.security.ExceptionHandler;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * DungeonExplorer - Manages the dungeon exploration gameplay
 *
 * This class handles:
 * - Room navigation
 * - Monster encounters
 * - Combat system
 * - Loot collection
 */
public class DungeonExplorer {

    private static final Logger logger = GameLogger.getLogger();
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private final MonsterFactory monsterFactory = new MonsterFactory();

    /**
     * Start exploring a dungeon with a character
     *
     * @param character The player's character
     * @param dungeon The dungeon to explore
     * @return true if dungeon was completed, false if player retreated or died
     */
    public boolean exploreDungeon(Character character, Dungeon dungeon) {
        try {
            System.out.println("\n=== ENTERING " + dungeon.getName().toUpperCase() + " ===");
            System.out.println(dungeon.getDescription());
            System.out.println("\nYour character: " + character);

            // Ask for confirmation
            System.out.print("\nAre you ready to enter? (y/n): ");
            String input = scanner.nextLine();
            if (!InputValidator.validateYesNo(input)) {
                System.out.println("You decide to prepare more before entering...");
                return false;
            }

            // Explore each room
            int totalGoldEarned = 0;
            List<Item> totalLoot = new ArrayList<>();

            for (int room = 1; room <= dungeon.getNumberOfRooms(); room++) {
                System.out.println("\n=== ROOM " + room + " of " + dungeon.getNumberOfRooms() + " ===");

                // Generate monsters for this room
                List<Monster> roomMonsters = generateRoomMonsters(dungeon);

                if (roomMonsters.isEmpty()) {
                    System.out.println("This room is empty. You move forward cautiously...");
                    continue;
                }

                System.out.println("You encounter " + roomMonsters.size() + " enemies!");
                for (Monster m : roomMonsters) {
                    System.out.println("- " + m);
                }

                // Fight all monsters in the room
                boolean survived = handleRoomCombat(character, roomMonsters, totalGoldEarned, totalLoot);

                if (!survived) {
                    System.out.println("\nYou have been defeated in the dungeon!");
                    return false;
                }

                // Rest between rooms if not the last room
                if (room < dungeon.getNumberOfRooms()) {
                    System.out.println("\nRoom cleared! You can rest before continuing.");
                    System.out.print("Rest to recover some stamina? (y/n): ");
                    input = scanner.nextLine();
                    if (InputValidator.validateYesNo(input)) {
                        int staminaRecovered = character.getMaxStamina() / 4;
                        character.restoreStamina(staminaRecovered);
                        System.out.println("You rest briefly and recover " + staminaRecovered + " stamina.");
                    }
                }
            }

            // Dungeon completed!
            System.out.println("\n=== DUNGEON COMPLETED! ===");
            System.out.println("You have conquered " + dungeon.getName() + "!");

            // Give rewards
            totalGoldEarned += dungeon.getGoldReward();
            System.out.println("\nTotal gold earned: " + totalGoldEarned);
            System.out.println("Experience gained: " + dungeon.getExperienceReward());

            if (!totalLoot.isEmpty()) {
                System.out.println("\nItems found:");
                for (Item item : totalLoot) {
                    System.out.println("- " + item);
                }
            }

            // Apply rewards (this would be expanded in full game)
            System.out.println("\n[Rewards will be added to your character]");

            logger.info("Dungeon completed: " + dungeon.getName() + " by " + character.getName());
            return true;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Error during dungeon exploration");
            return false;
        }
    }

    /**
     * Generate monsters for a room based on dungeon parameters
     */
    private List<Monster> generateRoomMonsters(Dungeon dungeon) {
        List<Monster> monsters = new ArrayList<>();

        // Determine number of monsters
        int monsterCount = dungeon.getMinMonstersPerRoom();
        if (dungeon.getMaxMonstersPerRoom() > dungeon.getMinMonstersPerRoom()) {
            int range = dungeon.getMaxMonstersPerRoom() - dungeon.getMinMonstersPerRoom() + 1;
            monsterCount += random.nextInt(range);
        }

        // Create monsters
        List<String> possibleTypes = dungeon.getPossibleMonsterTypes();
        for (int i = 0; i < monsterCount; i++) {
            String type = possibleTypes.get(random.nextInt(possibleTypes.size()));
            Monster monster = monsterFactory.createMonster(type, dungeon.getDifficulty());
            if (monster != null) {
                monsters.add(monster);
            }
        }

        return monsters;
    }

    /**
     * Handle combat in a room
     *
     * @return true if player survived, false if defeated
     */
    private boolean handleRoomCombat(Character character, List<Monster> monsters,
                                     int totalGoldEarned, List<Item> totalLoot) {

        System.out.println("\n--- COMBAT BEGINS ---");

        while (!monsters.isEmpty() && character.isAlive()) {
            // Show current status
            System.out.println("\nYour status: " + character);
            System.out.println("\nEnemies remaining: " + monsters.size());

            // Player turn
            System.out.println("\n--- YOUR TURN ---");
            System.out.println("1. Attack");
            System.out.println("2. Use Item (not implemented yet)");
            System.out.println("3. Try to flee");
            System.out.print("Your action: ");

            String input = scanner.nextLine();
            Integer choice = InputValidator.validateMenuChoice(input, 3);

            if (choice == null) {
                System.out.println("Invalid choice! You lose your turn!");
            } else {
                switch (choice) {
                    case 1:
                        // Attack first monster
                        if (!monsters.isEmpty()) {
                            Monster target = monsters.get(0);
                            int damage = character.attack();
                            if (damage > 0) {
                                target.takeDamage(damage);

                                // Check if monster defeated
                                if (!target.isAlive()) {
                                    monsters.remove(target);
                                    System.out.println("\nYou defeated " + target.getName() + "!");

                                    // Collect gold
                                    int gold = target.getGoldDrop();
                                    totalGoldEarned += gold;
                                    System.out.println("Gained " + gold + " gold!");

                                    // Collect items
                                    List<Item> drops = target.getDroppedItems();
                                    if (!drops.isEmpty()) {
                                        System.out.println("Items dropped:");
                                        for (Item item : drops) {
                                            System.out.println("- " + item);
                                            totalLoot.add(item);
                                        }
                                    }
                                }
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Item use not implemented yet!");
                        break;

                    case 3:
                        // Try to flee (50% chance)
                        if (random.nextInt(100) < 50) {
                            System.out.println("You managed to escape!");
                            return false;
                        } else {
                            System.out.println("Escape failed!");
                        }
                        break;
                }
            }

            // Monsters' turn
            if (!monsters.isEmpty()) {
                System.out.println("\n--- ENEMIES' TURN ---");
                for (Monster monster : monsters) {
                    if (monster.isAlive() && character.isAlive()) {
                        int damage = monster.attack();
                        character.takeDamage(damage);
                    }
                }
            }
        }

        return character.isAlive();
    }
}
