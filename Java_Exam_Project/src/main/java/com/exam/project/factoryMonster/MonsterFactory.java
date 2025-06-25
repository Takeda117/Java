package com.exam.project.factoryMonster;

import com.exam.project.security.ExceptionHandler;
import com.exam.project.logger.GameLogger;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Creates monsters with crash protection and input validation
 */
public class MonsterFactory {

    private static final Logger logger = GameLogger.getLogger();
    private static final Random random = new Random();

    /**
     * Creates a monster with full error handling and validation
     * @param monsterType The type of monster to create
     * @param difficulty The difficulty level (1-3)
     * @return A new monster instance, or null if creation fails
     */
    public AbstractMonster createMonster(String monsterType, int difficulty) {
        try {
            // Input validation
            if (monsterType == null || monsterType.trim().isEmpty()) {
                System.out.println("Error: Monster type cannot be empty!");
                logger.warning("createMonster called with null/empty type");
                return null;
            }

            // Sanitize and validate difficulty
            int safeDifficulty = Math.max(1, Math.min(3, difficulty));
            if (safeDifficulty != difficulty) {
                logger.info("Difficulty adjusted from " + difficulty + " to " + safeDifficulty);
            }

            // Clean input
            String cleanType = monsterType.trim().toLowerCase();
            AbstractMonster monster = null;

            // Create monster with error handling for each type
            switch (cleanType) {
                case "goblin":
                    monster = createGoblinSafely(safeDifficulty);
                    break;

                case "troll":
                    monster = createTrollSafely(safeDifficulty);
                    break;

                default:
                    System.out.println("Unknown monster type: " + monsterType);
                    logger.warning("Unknown monster type requested: " + monsterType);
                    return null;
            }

            // Final validation
            if (monster != null) {
                logger.info("Monster created successfully: " + monster.getName());
            } else {
                logger.warning("Monster creation returned null for type: " + cleanType);
            }

            return monster;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Failed to create monster");
            return null;
        }
    }

    /**
     * Creates a Goblin with error protection
     * @param difficulty The difficulty level
     * @return A Goblin instance or null if creation fails
     */
    private Goblin createGoblinSafely(int difficulty) {
        try {
            Goblin goblin = new Goblin(difficulty);
            logger.info("Goblin created: " + goblin.getName());
            return goblin;
        } catch (Exception e) {
            logger.severe("Failed to create Goblin: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Troll with error protection
     * @param difficulty The difficulty level
     * @return A Troll instance or null if creation fails
     */
    private Troll createTrollSafely(int difficulty) {
        try {
            Troll troll = new Troll(difficulty);
            logger.info("Troll created: " + troll.getName());
            return troll;
        } catch (Exception e) {
            logger.severe("Failed to create Troll: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a monster for Goblin Cave with validation
     * @param difficulty The difficulty level
     * @return A Goblin suitable for the cave
     */
    public AbstractMonster createGoblinCaveMonster(int difficulty) {
        try {
            return createMonster("Goblin", difficulty);
        } catch (Exception e) {
            logger.warning("Failed to create Goblin Cave monster, using fallback");
            return createMonster("Goblin", 1); // Safe fallback
        }
    }

    /**
     * Creates a monster for Swamp of Trolls with validation
     * @param difficulty The difficulty level
     * @return A Troll suitable for the swamp
     */
    public AbstractMonster createSwampMonster(int difficulty) {
        try {
            return createMonster("Troll", difficulty);
        } catch (Exception e) {
            logger.warning("Failed to create Swamp monster, using fallback");
            return createMonster("Troll", 1); // Safe fallback
        }
    }

    /**
     * Gets recommended monster count with bounds checking
     * @param dungeonName The name of the dungeon
     * @param roomNumber The room number (unused but kept for interface)
     * @return Safe monster count between 1-3
     */
    public static int getRecommendedMonsterCount(String dungeonName, int roomNumber) {
        try {
            if (dungeonName == null || dungeonName.trim().isEmpty()) {
                return 1; // Safe default
            }

            String dungeonLower = dungeonName.toLowerCase();

            if (dungeonLower.contains("goblin")) {
                return 2; // 2 Goblins per room
            } else if (dungeonLower.contains("troll") || dungeonLower.contains("swamp")) {
                return 1; // 1 Troll per room
            } else {
                return 1; // Safe default
            }

        } catch (Exception e) {
            logger.warning("Error calculating monster count: " + e.getMessage());
            return 1; // Safe fallback
        }
    }

    /**
     * Shows available monster types with error protection
     */
    public void showAvailableTypes() {
        try {
            System.out.println("\n=== AVAILABLE MONSTER TYPES ===");
            System.out.println("1. GOBLIN - Weak but numerous enemies");
            System.out.println("2. TROLL - Strong and dangerous enemies");
            System.out.println("Monster strength scales with difficulty level (1-3)");
        } catch (Exception e) {
            System.out.println("Error displaying monster types.");
            logger.warning("Error in showAvailableTypes: " + e.getMessage());
        }
    }
}