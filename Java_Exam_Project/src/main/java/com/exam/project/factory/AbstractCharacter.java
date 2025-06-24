package com.exam.project.factory;

/**
 * AbstractCharacter - Base class for all characters
 * Contains common behavior that all characters share
 */
public abstract class AbstractCharacter implements Character {

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
     * Constructor for creating a character with basic stats
     */
    protected AbstractCharacter(String name, int baseHealth, int baseStamina, int baseDamage) {
        this.name = name;
        this.health = baseHealth;
        this.maxHealth = baseHealth;
        this.stamina = baseStamina;
        this.maxStamina = baseStamina;
        this.baseDamage = baseDamage;
        this.money = 100;  // All characters start with 100 money
        this.level = 1;    // All characters start at level 1
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            System.out.println("Invalid damage amount ignored");
            return;
        }

        this.health = Math.max(0, this.health - damage);
        System.out.printf("%s takes %d damage! Health: %d/%d%n",
                name, damage, health, maxHealth);

        if (!isAlive()) {
            System.out.printf("%s has been defeated!%n", name);
        }
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void train() {
        if (stamina < 10) {
            System.out.printf("%s doesn't have enough stamina to train!%n", name);
            return;
        }

        // Each character type trains differently (implemented in subclasses)
        performTraining();

        // Common training effects
        stamina -= 10;
        maxStamina = Math.max(50, maxStamina - 2);  // Training penalty

        System.out.printf("%s finished training! Stamina: %d/%d%n",
                name, stamina, maxStamina);
    }

    /**
     * Abstract method - each character type implements this differently
     */
    protected abstract void performTraining();

    @Override
    public void restoreStamina(int amount) {
        if (amount < 0) {
            System.out.println("Cannot restore negative stamina");
            return;
        }

        this.stamina = Math.min(maxStamina, this.stamina + amount);
    }

    @Override
    public void rest() {
        this.health = maxHealth;
        this.stamina = maxStamina;
        System.out.printf("%s takes a rest and fully recovers!%n", name);
    }

    // Getter methods - all characters need these
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getStamina() {
        return stamina;
    }

    @Override
    public int getMaxStamina() {
        return maxStamina;
    }

    @Override
    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                name, health, maxHealth, stamina, maxStamina, baseDamage, money, level);
    }
}