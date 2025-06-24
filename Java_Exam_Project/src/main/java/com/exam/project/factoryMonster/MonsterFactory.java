package com.exam.project.factoryMonster;

import com.exam.project.security.ExceptionHandler;
import com.exam.project.iterator.Item;
import com.exam.project.logger.GameLogger;
import java.util.Random;
import java.util.logging.Logger;

/**
 * MonsterFactory - Creates different types of monsters for dungeons
 *
 * Uses Factory Pattern to create various monster types
 * Each monster type has different stats based on difficulty
 */
public class MonsterFactory {

    private static final Logger logger = GameLogger.getLogger();
    private static final Random random = new Random();

    /**
     * Create a monster based on type and difficulty
     *
     * @param monsterType The type of monster (e.g., "Goblin", "Orc")
     * @param difficulty The difficulty level (affects stats)
     * @return A new Monster instance, or null if creation fails
     */
    public Monster createMonster(String monsterType, int difficulty) {
        try {
            // Validate inputs
            if (monsterType == null || monsterType.trim().isEmpty()) {
                System.out.println("Monster type cannot be empty!");
                return null;
            }

            if (difficulty < 1 || difficulty > 10) {
                difficulty = 1; // Default to easiest
            }

            String type = monsterType.trim();
            Monster monster = null;

            // Create monster based on type
            switch (type.toLowerCase()) {
                case "goblin":
                    monster = createGoblin(difficulty);
                    break;

                case "orc":
                    monster = createOrc(difficulty);
                    break;

                case "skeleton":
                    monster = createSkeleton(difficulty);
                    break;

                case "wolf":
                    monster = createWolf(difficulty);
                    break;

                default:
                    // If unknown type, create a generic goblin
                    System.out.println("Unknown monster type: " + type + ". Creating goblin instead.");
                    monster = createGoblin(difficulty);
                    break;
            }

            // Log creation
            if (monster != null) {
                logger.info("Created monster: " + monster.getName() + " (Type: " + monster.getType() + ")");
            }

            return monster;

        } catch (Exception e) {
            ExceptionHandler.handleException(e, "Failed to create monster");
            return null;
        }
    }

    /**
     * Create a Goblin - weak but common enemy
     */
    private Monster createGoblin(int difficulty) {
        // Generate a random goblin name
        String[] goblinNames = {"Gruk", "Snarl", "Grik", "Zog", "Mog"};
        String name = goblinNames[random.nextInt(goblinNames.length)];

        // Stats based on difficulty
        int health = 20 + (difficulty * 5);
        int damage = 5 + (difficulty * 2);
        int goldDrop = 10 + (difficulty * 5);

        Monster goblin = new Monster(name + " the Goblin", "Goblin", health, damage, goldDrop);

        // Add possible drops
        if (random.nextInt(100) < 50) { // 50% chance to have a potion
            goblin.addPossibleDrop(new Item("Small Health Potion", Item.ItemType.POTION, 25, 0));
        }

        return goblin;
    }

    /**
     * Create an Orc - stronger enemy
     */
    private Monster createOrc(int difficulty) {
        String[] orcNames = {"Grok", "Thrak", "Ugor", "Brak", "Durg"};
        String name = orcNames[random.nextInt(orcNames.length)];

        // Orcs are tougher than goblins
        int health = 40 + (difficulty * 8);
        int damage = 8 + (difficulty * 3);
        int goldDrop = 20 + (difficulty * 8);

        Monster orc = new Monster(name + " the Orc", "Orc", health, damage, goldDrop);

        // Orcs have better drops
        if (random.nextInt(100) < 30) { // 30% chance for weapon
            orc.addPossibleDrop(new Item("Rusty Sword", Item.ItemType.WEAPON, 50, 3));
        }

        return orc;
    }

    /**
     * Create a Skeleton - undead enemy
     */
    private Monster createSkeleton(int difficulty) {
        // Skeletons don't have names, just types
        String[] types = {"Warrior", "Archer", "Guard"};
        String type = types[random.nextInt(types.length)];

        // Skeletons have less health but decent damage
        int health = 25 + (difficulty * 4);
        int damage = 7 + (difficulty * 2);
        int goldDrop = 15 + (difficulty * 3); // Less gold (they're dead!)

        Monster skeleton = new Monster("Skeleton " + type, "Skeleton", health, damage, goldDrop);

        // Skeletons might drop bones (misc item)
        if (random.nextInt(100) < 40) {
            skeleton.addPossibleDrop(new Item("Ancient Bone", Item.ItemType.MISC, 20, 0));
        }

        return skeleton;
    }

    /**
     * Create a Wolf - beast enemy
     */
    private Monster createWolf(int difficulty) {
        String[] wolfTypes = {"Grey", "Black", "White", "Dire"};
        String type = wolfTypes[random.nextInt(wolfTypes.length)];

        // Wolves are fast but not too tough
        int health = 30 + (difficulty * 4);
        int damage = 6 + (difficulty * 3);
        int goldDrop = 5 + (difficulty * 2); // Wolves don't carry much gold

        Monster wolf = new Monster(type + " Wolf", "Wolf", health, damage, goldDrop);

        // Wolves might drop pelts
        if (random.nextInt(100) < 35) {
            wolf.addPossibleDrop(new Item("Wolf Pelt", Item.ItemType.MISC, 30, 0));
        }

        return wolf;
    }

    /**
     * Create a random monster from available types
     */
    public Monster createRandomMonster(int difficulty) {
        String[] types = {"Goblin", "Orc", "Skeleton", "Wolf"};
        String randomType = types[random.nextInt(types.length)];

        return createMonster(randomType, difficulty);
    }

    /**
     * Show available monster types
     */
    public void showAvailableTypes() {
        System.out.println("\nAvailable monster types:");
        System.out.println("- Goblin: Weak but common enemies");
        System.out.println("- Orc: Stronger warriors");
        System.out.println("- Skeleton: Undead guards");
        System.out.println("- Wolf: Fast beasts");
    }
}
