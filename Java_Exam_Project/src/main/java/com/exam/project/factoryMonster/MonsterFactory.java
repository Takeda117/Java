package com.exam.project.factoryMonster;

import com.exam.project.logger.GameLogger;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Factory for creating monsters
 */
public class MonsterFactory {

    private static final Logger logger = GameLogger.getLogger();
    private Random random = new Random();

    /**
     * Create a monster
     */
    public AbstractMonster createMonster(String type, int difficulty) {
        logger.info("Creating monster: type=" + type + ", difficulty=" + difficulty);

        if (type == null) {
            logger.warning("Monster creation failed: null type");
            System.out.println("Monster type is null!");
            return null;
        }

        try {
            // Make sure difficulty is ok
            int safeDifficulty = difficulty;
            if (difficulty < 1) {
                safeDifficulty = 1;
                logger.info("Difficulty adjusted from " + difficulty + " to 1");
            }
            if (difficulty > 3) {
                safeDifficulty = 3;
                logger.info("Difficulty adjusted from " + difficulty + " to 3");
            }

            String cleanType = type.trim().toLowerCase();

            switch (cleanType) {
                case "goblin":
                    try {
                        Goblin goblin = new Goblin(safeDifficulty);
                        logger.info("Goblin created: " + goblin.getName());
                        return goblin;
                    } catch (Exception e) {
                        logger.severe("Failed to create Goblin: " + e.getMessage());
                        return null;
                    }

                case "troll":
                    try {
                        Troll troll = new Troll(safeDifficulty);
                        logger.info("Troll created: " + troll.getName());
                        return troll;
                    } catch (Exception e) {
                        logger.severe("Failed to create Troll: " + e.getMessage());
                        return null;
                    }

                default:
                    logger.warning("Unknown monster type requested: " + type);
                    System.out.println("Unknown monster: " + type);
                    return null;
            }
        } catch (Exception e) {
            logger.severe("Unexpected error creating monster: " + e.getMessage());
            System.out.println("Monster creation failed!");
            return null;
        }
    }

    /**
     * Create goblin for goblin cave
     */
    public AbstractMonster createGoblinCaveMonster(int difficulty) {
        logger.info("Creating Goblin Cave monster with difficulty " + difficulty);
        try {
            return createMonster("goblin", difficulty);
        } catch (Exception e) {
            logger.warning("Failed to create Goblin Cave monster: " + e.getMessage());
            return createMonster("goblin", 1); // fallback
        }
    }

    /**
     * Create troll for swamp
     */
    public AbstractMonster createSwampMonster(int difficulty) {
        logger.info("Creating Swamp monster with difficulty " + difficulty);
        try {
            return createMonster("troll", difficulty);
        } catch (Exception e) {
            logger.warning("Failed to create Swamp monster: " + e.getMessage());
            return createMonster("troll", 1); // fallback
        }
    }

    /**
     * Show available monsters
     */
    public void showAvailableTypes() {
        logger.info("Displaying available monster types");
        try {
            System.out.println("\n=== MONSTERS ===");
            System.out.println("1. GOBLIN - Weak enemies (Goblin Cave)");
            System.out.println("2. TROLL - Strong enemies (Swamp of Trolls)");
            System.out.println("\nDifficulty: 1 = Easy, 2 = Medium, 3 = Hard");
        } catch (Exception e) {
            logger.warning("Error displaying monster types: " + e.getMessage());
            System.out.println("Error showing monster info!");
        }
    }
}