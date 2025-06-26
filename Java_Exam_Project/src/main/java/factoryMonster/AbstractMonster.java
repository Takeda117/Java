package java.factoryMonster;

import java.iterator.Item;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.logger.GameLogger;
import java.util.logging.Logger;

/**
 * AbstractMonster - Base class for all monsters in the game
 * <p>
 * This class implements common behavior for all monsters,
 * while subclasses define specific characteristics.
 * </p>
 * <p>
 * The structure is similar to characters but simpler,
 * as monsters don't have complex inventories or progression.
 * </p>
 */
public abstract class AbstractMonster {

    private static final Logger logger = GameLogger.getLogger();

    // Base statistics common to all monsters
    protected String name;
    protected String type;
    protected int health;
    protected int maxHealth;
    protected int baseDamage;
    protected int goldDrop;

    // Item drop system
    protected List<Item> possibleDrops;
    protected int dropChance; // Drop probability percentage (0-100)

    // Random number generator for damage variations and drops
    protected static final Random random = new Random();

    /**
     * Protected constructor - only subclasses can create monsters
     *
     * @param name Specific monster name (e.g., "Gruk")
     * @param type Monster type (e.g., "Goblin")
     * @param health Monster's hit points
     * @param baseDamage Monster's base damage
     * @param goldDrop Gold dropped when defeated
     * @param dropChance Probability of dropping items (0-100)
     * @throws IllegalArgumentException If any parameters are invalid
     */
    protected AbstractMonster(String name, String type, int health, int baseDamage, int goldDrop, int dropChance) {
        // Input validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Monster name cannot be empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Monster type cannot be empty");
        }
        if (health <= 0 || baseDamage < 0 || goldDrop < 0) {
            throw new IllegalArgumentException("Monster statistics must be positive");
        }

        this.name = name.trim();
        this.type = type.trim();
        this.health = health;
        this.maxHealth = health;
        this.baseDamage = baseDamage;
        this.goldDrop = goldDrop;
        this.dropChance = Math.max(0, Math.min(100, dropChance)); // Ensure between 0 and 100
        this.possibleDrops = new ArrayList<>();
    }

    /**
     * Monster attack - base behavior
     * Subclasses can override this method for special behaviors
     *
     * @return the damage inflicted by the attack
     */
    public int attack() {
        // Calculate damage with a small random variation
        int damage = calculateDamage();

        // Attack message - subclasses can customize it
        System.out.printf("%s %s attacks for %d damage!%n", type, name, damage);

        return damage;
    }

    /**
     * Calculates attack damage with random variation
     * Protected method that subclasses can override
     * 
     * @return The calculated damage amount
     */
    protected int calculateDamage() {
        // Random variation of ±20% of base damage
        int variance = (int)(baseDamage * 0.2); // 20% of base damage
        int variation = random.nextInt(variance * 2 + 1) - variance; // From -variance to +variance

        return Math.max(1, baseDamage + variation); // At least 1 damage
    }

    /**
     * Monster takes damage from a player attack
     *
     * @param damage the amount of damage received
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            logger.warning("Invalid negative damage attempted: " + damage + " on " + type + " " + name);
            System.out.println("Invalid damage ignored");
            return;
        }

        int oldHealth = this.health;
        this.health = Math.max(0, this.health - damage);
        
        logger.info(type + " " + name + " took " + damage + " damage. Health: " + oldHealth + " -> " + health);

        System.out.printf("%s %s takes %d damage! Health: %d/%d%n",
                type, name, damage, health, maxHealth);

        if (!isAlive()) {
            logger.info(type + " " + name + " was defeated");
            System.out.printf("%s %s has been defeated!%n", type, name);
            onDefeat(); // Call defeat method
        }
    }

    /**
     * Checks if the monster is still alive
     *
     * @return true if health > 0, false otherwise
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Method called when the monster is defeated
     * Subclasses can override for special behaviors
     */
    protected void onDefeat() {
        // Base behavior: defeat message
        // Subclasses can add special effects
    }

    /**
     * Adds a possible item to the monster's drop list
     *
     * @param item the item to add to the possible drops list
     */
    public void addPossibleDrop(Item item) {
        if (item != null) {
            possibleDrops.add(item);
        }
    }

    /**
     * Calculates items dropped by the monster when defeated
     * Uses probability system based on dropChance
     *
     * @return list of actually dropped items
     */
    public List<Item> getDroppedItems() {
        List<Item> actualDrops = new ArrayList<>();

        // If no possible drops, return empty list
        if (possibleDrops.isEmpty()) {
            return actualDrops;
        }

        // For each possible drop, check if it's actually dropped
        for (Item item : possibleDrops) {
            if (random.nextInt(100) < dropChance) {
                actualDrops.add(item);
            }
        }

        return actualDrops;
    }

    // === Getter methods for monster properties ===

    /**
     * Gets the monster's name
     * 
     * @return The monster's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the monster's type
     * 
     * @return The monster's type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the monster's current health
     * 
     * @return The current health value
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the monster's maximum health
     * 
     * @return The maximum health value
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the monster's base damage
     * 
     * @return The base damage value
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Gets the gold dropped when defeated
     * 
     * @return The gold drop amount
     */
    public int getGoldDrop() {
        return goldDrop;
    }

    /**
     * Gets the drop chance percentage
     * 
     * @return The drop chance (0-100)
     */
    public int getDropChance() {
        return dropChance;
    }

    /**
     * Returns a safe copy of the possible drops list
     * 
     * @return Copy of the possible drops list
     */
    public List<Item> getPossibleDrops() {
        return new ArrayList<>(possibleDrops);
    }

    /**
     * Crash-safe toString implementation
     * 
     * @return Formatted string with monster stats
     */
    @Override
    public String toString() {
        try {
            String safeName = (name != null) ? name : "Unknown";
            String safeType = (type != null) ? type : "Monster";
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeBaseDamage = Math.max(0, baseDamage);
            int safeGoldDrop = Math.max(0, goldDrop);
            int safeDropChance = Math.max(0, Math.min(100, dropChance));

            return String.format("%s %s [Health: %d/%d, Damage: %d, Gold: %d, Drop: %d%%]",
                    safeType, safeName, safeHealth, safeMaxHealth, safeBaseDamage, safeGoldDrop, safeDropChance);
        } catch (Exception e) {
            return "Monster [Error displaying stats]";
        }
    }

    /**
     * Comparison between monsters based on name and type
     * 
     * @param obj The object to compare with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractMonster monster = (AbstractMonster) obj;
        return name.equals(monster.name) && type.equals(monster.type);
    }

    /**
     * Generates a hash code for the monster
     * 
     * @return The hash code value
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}