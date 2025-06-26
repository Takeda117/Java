package com.exam.project.factory;

import com.exam.project.iterator.Item;

/**
 * Base class for all characters
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
     * Constructor
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
     * Takes damage
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
     * @return true if health > 0, false otherwise
     */
    @Override
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Restores stamina
     * @param amount Amount of stamina to restore (negative values reduce stamina)
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
     * Rest to recover stamina
     */
    @Override
    public void rest() {
        stamina = maxStamina;
        System.out.printf("%s rests and recovers stamina.%n", name);
    }

    /**
     * Train to improve stats
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
     * Abstract method for subclass-specific training
     */
    protected abstract void performTraining();

    /**
     * Equip an item
     * @param item The item to equip
     */
    @Override
    public abstract void equipItem(Item item);

    /**
     * Add an item to inventory
     * @param item The item to add
     */
    @Override
    public abstract void addItem(Item item);

    /**
     * Show inventory contents
     */
    @Override
    public abstract void showInventory();

    // Getters
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
}
