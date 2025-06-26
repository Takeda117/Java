package com.exam.project.factoryMonster;

import com.exam.project.iterator.Item;
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
        
        // Ensure difficulty is in valid range
        int safeDifficulty = Math.max(1, Math.min(3, difficulty));
        if (safeDifficulty != difficulty) {
            logger.warning("Invalid difficulty level adjusted: " + difficulty + " -> " + safeDifficulty);
        }
        
        try {
            String cleanType = type.trim().toLowerCase();

            switch (cleanType) {
                case "goblin":
                    try {
                        // Use the generic Monster class for goblins
                        String name = generateGoblinName();
                        int health = 15 + (safeDifficulty * 5);
                        int damage = 3 + safeDifficulty;
                        int goldDrop = 10 + (safeDifficulty * 5);
                        
                        Monster goblin = new Monster(name, "Goblin", health, damage, goldDrop);
                        

                        
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
            logger.severe("Error creating monster: " + e.getMessage());
            System.out.println("Failed to create monster!");
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

    /**
     * Generate a random goblin name
     */
    private String generateGoblinName() {
        String[] names = {"Gruk", "Zik", "Mog", "Blat", "Grizz", "Snot", "Gnarl", "Skiz", "Fizzle", "Grump"};
        return names[random.nextInt(names.length)];
    }
}
