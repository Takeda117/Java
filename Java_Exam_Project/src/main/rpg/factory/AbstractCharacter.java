package rpg.factory;

import rpg.iterator.Item;

/**
 * Base class for all characters
 * <p>
 * This abstract class implements the Character interface and provides
 * common functionality for all character types. It serves as part of the
 * Factory Pattern implementation by defining the template for concrete
 * character classes.
 * </p>
 */
public abstract class AbstractCharacter implements Character {
    
    protected String name;
    protected int health;
    protected int maxHealth;
    protected int stamina;
    protected int maxStamina;
    protected int baseDamage;
    protected int money;
    protected int level;

    /**
     * Constructor for creating a new character
     * 
     * @param name The character's name
     * @param baseHealth The initial and maximum health
     * @param baseStamina The initial and maximum stamina
     * @param baseDamage The base damage value
     */
    protected AbstractCharacter(String name, int baseHealth, int baseStamina, int baseDamage) {
        this.name = (name != null && !name.trim().isEmpty()) ? name.trim() : "Unknown";
        this.health = Math.max(1, baseHealth);
        this.maxHealth = this.health;
        this.stamina = Math.max(1, baseStamina);
        this.maxStamina = this.stamina;
        this.baseDamage = Math.max(1, baseDamage);
        this.money = 100;
        this.level = 1;
    }

    /**
     * Takes damage from an attack
     * 
     * @param damage Amount of damage to take
     */
    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            return;
        }

        this.health = Math.max(0, this.health - damage);
        System.out.printf("%s takes %d damage! Health: %d/%d%n",
                name, damage, health, maxHealth);

        if (!isAlive()) {
            System.out.printf("%s has been defeated!%n", name);
        }
    }

    /**
     * Checks if character is alive
     * 
     * @return true if health > 0, false otherwise
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Restores or reduces stamina
     * 
     * @param amount Amount of stamina to restore (positive) or reduce (negative)
     */
    @Override
    public void restoreStamina(int amount) {
        int oldStamina = stamina;
        
        if (amount > 0) {
            // Restore stamina (positive amount)
            stamina = Math.min(maxStamina, stamina + amount);
            
            int restored = stamina - oldStamina;
            if (restored > 0) {
                System.out.printf("%s restored %d stamina. Stamina: %d/%d%n", 
                        name, restored, stamina, maxStamina);
            }
        } else if (amount < 0) {
            // Reduce stamina (negative amount)
            int reduction = Math.abs(amount);
            stamina = Math.max(0, stamina - reduction);
            
            int reduced = oldStamina - stamina;
            if (reduced > 0) {
                System.out.printf("%s used %d stamina. Stamina: %d/%d%n", 
                        name, reduced, stamina, maxStamina);
            }
        }
    }

    /**
     * Rests to recover stamina
     */
    @Override
    public void rest() {
        stamina = maxStamina;
        System.out.printf("%s rests and recovers stamina.%n", name);
    }

    /**
     * Trains to improve stats
     */
    @Override
    public void train() {
        if (money < 50) {
            System.out.println("Not enough money to train!");
            return;
        }
        
        money -= 50;
        performTraining();
        level++;
        
        System.out.printf("%s is now level %d!%n", name, level);
    }

    /**
     * Abstract method for subclass-specific training implementation
     */
    protected abstract void performTraining();

    /**
     * Equips an item
     * 
     * @param item The item to equip
     */
    @Override
    public abstract void equipItem(Item item);

    /**
     * Adds an item to inventory
     * 
     * @param item The item to add
     */
    @Override
    public abstract void addItem(Item item);

    /**
     * Shows inventory contents
     */
    @Override
    public abstract void showInventory();

    /**
     * Gets the character's name
     * 
     * @return The character's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the character's current health
     * 
     * @return The current health value
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * Gets the character's maximum health
     * 
     * @return The maximum health value
     */
    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the character's current stamina
     * 
     * @return The current stamina value
     */
    @Override
    public int getStamina() {
        return stamina;
    }

    /**
     * Gets the character's maximum stamina
     * 
     * @return The maximum stamina value
     */
    @Override
    public int getMaxStamina() {
        return maxStamina;
    }

    /**
     * Gets the character's base damage
     * 
     * @return The base damage value
     */
    @Override
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Gets the character's money
     * 
     * @return The amount of money
     */
    @Override
    public int getMoney() {
        return money;
    }

    /**
     * Gets the character's level
     * 
     * @return The character's level
     */
    @Override
    public int getLevel() {
        return level;
    }
}
