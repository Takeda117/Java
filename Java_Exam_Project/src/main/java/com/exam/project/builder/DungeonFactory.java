package com.exam.project.builder;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * DungeonFactory - Creates exactly TWO dungeons using Builder Pattern
 */
public class DungeonFactory {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Creates Goblin Cave using Builder Pattern
     */
    public static Dungeon createGoblinCave() {
        try {
            return new Dungeon.DungeonBuilder("Goblin Cave")
                    .withDescription("A dark cave filled with goblins. Perfect for beginners.")
                    .withRooms(4)
                    .withGoldReward(150)
                    .withExperienceReward(100)
                    .addMonsterType("Goblin")
                    .build();

        } catch (Exception e) {
            logger.severe("Error creating Goblin Cave: " + e.getMessage());
            // Emergency fallback
            return new Dungeon.DungeonBuilder("Emergency Cave")
                    .addMonsterType("Goblin")
                    .build();
        }
    }

    /**
     * Creates Swamp of Trolls using Builder Pattern
     */
    public static Dungeon createSwampOfTrolls() {
        try {
            return new Dungeon.DungeonBuilder("Swamp of Trolls")
                    .withDescription("A dangerous swamp where powerful trolls lurk. For experienced adventurers.")
                    .withRooms(6)
                    .withGoldReward(400)
                    .withExperienceReward(300)
                    .addMonsterType("Troll")
                    .build();

        } catch (Exception e) {
            logger.severe("Error creating Swamp of Trolls: " + e.getMessage());
            // Emergency fallback
            return new Dungeon.DungeonBuilder("Emergency Swamp")
                    .addMonsterType("Troll")
                    .build();
        }
    }

    /**
     * Shows the TWO available dungeons
     */
    public static void showAvailableDungeons() {
        try {
            System.out.println("\n=== AVAILABLE DUNGEONS ===\n");

            Dungeon goblinCave = createGoblinCave();
            System.out.println("1. " + goblinCave.getName());
            System.out.println("   " + goblinCave.toString());
            System.out.println("   Difficulty: ★☆☆ (Beginner)");

            System.out.println();

            Dungeon trollSwamp = createSwampOfTrolls();
            System.out.println("2. " + trollSwamp.getName());
            System.out.println("   " + trollSwamp.toString());
            System.out.println("   Difficulty: ★★★ (Advanced)");

            System.out.println("\nChoose based on your experience level!");

        } catch (Exception e) {
            logger.severe("Error showing dungeons: " + e.getMessage());
            System.out.println("Error displaying dungeons.");
        }
    }

    /**
     * Creates dungeon by choice - ONLY 1 or 2
     */
    public static Dungeon createDungeonByChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    System.out.println("\nYou chose the Goblin Cave!");
                    return createGoblinCave();

                case 2:
                    System.out.println("\nYou chose the Swamp of Trolls!");
                    return createSwampOfTrolls();

                default:
                    System.out.println("Invalid choice! Using Goblin Cave.");
                    return createGoblinCave();
            }
        } catch (Exception e) {
            logger.severe("Error creating dungeon: " + e.getMessage());
            return createGoblinCave();
        }
    }
}