package com.exam.project.factory;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Abstract base class for all characters with comprehensive crash protection
 */
public abstract class AbstractCharacter implements Character {

    private static final Logger logger = GameLogger.getLogger();

    // Protected fields - subclasses can access these
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int stamina;
    protected int maxStamina;
    protected int baseDamage;
    protected int money;
    protected int level;

    /**
     * Constructor with input validation and crash protection
     * @param name Character name (will be sanitized)
     * @param baseHealth Starting health (minimum 1)
     * @param baseStamina Starting stamina (minimum 1)
     * @param baseDamage Starting damage (minimum 1)
     */
    protected AbstractCharacter(String name, int baseHealth, int baseStamina, int baseDamage) {
        try {
            // Sanitize and validate inputs
            this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "Unknown Character";
            this.health = Math.max(1, baseHealth);
            this.maxHealth = this.health;
            this.stamina = Math.max(1, baseStamina);
            this.maxStamina = this.stamina;
            this.baseDamage = Math.max(1, baseDamage);
            this.money = 100;  // Safe default
            this.level = 1;    // Safe default

            logger.info("Character created: " + this.name);

        } catch (Exception e) {
            // Emergency fallback values
            logger.severe("Error creating character, using fallback values: " + e.getMessage());
            this.name = "Error Character";
            this.health = this.maxHealth = 50;
            this.stamina = this.maxStamina = 50;
            this.baseDamage = 5;
            this.money = 100;
            this.level = 1;
        }
    }

    /**
     * Takes damage with comprehensive bounds checking
     * @param damage Amount of damage to take
     */
    @Override
    public void takeDamage(int damage) {
        try {
            if (damage < 0) {
                System.out.println("Invalid damage amount ignored");
                logger.warning("Negative damage attempted: " + damage);
                return;
            }

            int oldHealth = this.health;
            this.health = Math.max(0, this.health - damage);

            String safeName = (name != null) ? name : "Character";
            System.out.printf("%s takes %d damage! Health: %d/%d%n",
                    safeName, damage, Math.max(0, health), Math.max(1, maxHealth));

            if (!isAlive()) {
                System.out.printf("%s has been defeated!%n", safeName);
                logger.info("Character defeated: " + safeName);
            }

        } catch (Exception e) {
            logger.severe("Error in takeDamage: " + e.getMessage());
            System.out.println("Damage calculation error occurred.");
        }
    }

    /**
     * Checks if character is alive with error protection
     * @return true if health > 0, false otherwise or on error
     */
    @Override
    public boolean isAlive() {
        try {
            return health > 0;
        } catch (Exception e) {
            logger.warning("Error checking if character is alive: " + e.getMessage());
            return false; // Safe assumption for error state
        }
    }

    /**
     * Character training with error protection
     */
    @Override
    public void train() {
        try {
            if (stamina < 10) {
                String safeName = (name != null) ? name : "Character";
                System.out.printf("%s doesn't have enough stamina to train!%n", safeName);
                return;
            }

            // Perform subclass-specific training
            performTraining();

            // Common training effects with bounds checking
            stamina = Math.max(0, stamina - 10);
            maxStamina = Math.max(10, maxStamina - 2);  // Minimum 10 stamina

            String safeName = (name != null) ? name : "Character";
            System.out.printf("%s finished training! Stamina: %d/%d%n",
                    safeName, Math.max(0, stamina), Math.max(1, maxStamina));

        } catch (Exception e) {
            logger.severe("Error during training: " + e.getMessage());
            System.out.println("Training failed due to error.");
        }
    }

    /**
     * Abstract method for subclass-specific training
     */
    protected abstract void performTraining();

    /**
     * Restores stamina with bounds checking
     * @param amount Amount to restore (negative values will reduce stamina)
     */
    @Override
    public void restoreStamina(int amount) {
        try {
            int oldStamina = this.stamina;

            if (amount >= 0) {
                // Restoring stamina
                this.stamina = Math.min(maxStamina, this.stamina + amount);
            } else {
                // Consuming stamina (negative amount)
                this.stamina = Math.max(0, this.stamina + amount);
            }

            // Optional logging for debugging
            if (amount != 0) {
                logger.fine(String.format("Stamina changed from %d to %d (amount: %d)",
                        oldStamina, stamina, amount));
            }

        } catch (Exception e) {
            logger.warning("Error restoring stamina: " + e.getMessage());
        }
    }

    /**
     * Full rest with error protection
     */
    @Override
    public void rest() {
        try {
            this.health = maxHealth;
            this.stamina = maxStamina;
            String safeName = (name != null) ? name : "Character";
            System.out.printf("%s takes a rest and fully recovers!%n", safeName);
        } catch (Exception e) {
            logger.warning("Error during rest: " + e.getMessage());
            System.out.println("Rest completed with some difficulties.");
        }
    }

    // Safe getter methods with bounds checking
    @Override
    public String getName() {
        return (name != null) ? name : "Unknown";
    }

    @Override
    public int getHealth() {
        return Math.max(0, health);
    }

    @Override
    public int getMaxHealth() {
        return Math.max(1, maxHealth);
    }

    @Override
    public int getStamina() {
        return Math.max(0, stamina);
    }

    @Override
    public int getMaxStamina() {
        return Math.max(1, maxStamina);
    }

    @Override
    public int getBaseDamage() {
        return Math.max(1, baseDamage);
    }

    @Override
    public int getMoney() {
        return Math.max(0, money);
    }

    @Override
    public int getLevel() {
        return Math.max(1, level);
    }

    /**
     * Crash-safe toString implementation
     * @return Character description or error message
     */
    @Override
    public String toString() {
        try {
            String safeName = (name != null) ? name : "Unknown";
            int safeHealth = Math.max(0, health);
            int safeMaxHealth = Math.max(1, maxHealth);
            int safeStamina = Math.max(0, stamina);
            int safeMaxStamina = Math.max(1, maxStamina);
            int safeBaseDamage = Math.max(1, baseDamage);
            int safeMoney = Math.max(0, money);
            int safeLevel = Math.max(1, level);

            return String.format("%s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                    safeName, safeHealth, safeMaxHealth, safeStamina, safeMaxStamina,
                    safeBaseDamage, safeMoney, safeLevel);
        } catch (Exception e) {
            return "Character [Error displaying stats]";
        }
    }
}