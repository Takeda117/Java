package com.exam.project.factory;

import com.exam.project.iterator.Item;
import com.exam.project.iterator.Inventory;
import java.util.Random;

/**
 * Warrior character class
 */
public class Warrior extends AbstractCharacter {

    private static final Random random = new Random();
    private final Inventory inventory;

    /**
     * Creates a new Warrior
     * @param name The warrior's name
     */
    public Warrior(String name) {
        super(name, 120, 100, 15);
        this.inventory = new Inventory(25);
    }

    /**
     * Warrior attack
     * @return Damage dealt
     */
    @Override
    public int attack() {
        if (stamina < 5) {
            System.out.printf("%s is too tired to attack!%n", name);
            return 0;
        }

        stamina -= 5;

        int equipmentBonus = inventory != null ? inventory.getTotalStatBonus() : 0;
        int damage = baseDamage + equipmentBonus + random.nextInt(5);
        
        System.out.printf("%s attacks for %d damage!%n", name, damage);
        return Math.max(1, damage);
    }

    /**
     * Warrior training
     */
    @Override
    protected void performTraining() {
        baseDamage += 2;
        maxHealth += 5;
        health = maxHealth;
        System.out.printf("%s trains with weapons!%n", name);
    }

    /**
     * Equips an item
     * @param item The item to equip
     */
    @Override
    public void equipItem(Item item) {
        if (item == null || inventory == null || !item.isEquippable()) {
            return;
        }

        if (!inventory.getAllItems().contains(item)) {
            System.out.println("Item not in inventory!");
            return;
        }

        inventory.equipItem(item);
        System.out.printf("%s equipped %s!%n", name, item.getName());
    }

    /**
     * Adds an item
     * @param item The item to add
     */
    @Override
    public void addItem(Item item) {
        if (item == null || inventory == null) {
            return;
        }

        if (inventory.addItem(item)) {
            System.out.printf("%s added %s!%n", name, item.getName());
        }
    }

    /**
     * Shows inventory
     */
    @Override
    public void showInventory() {
        if (inventory == null) {
            return;
        }

        System.out.printf("\n=== %s's Equipment ===%n", name);
        inventory.displayInventory();

        int totalBonus = inventory.getTotalStatBonus();
        if (totalBonus > 0) {
            System.out.printf("Equipment bonus: +%d damage%n", totalBonus);
        }
    }

    /**
     * Gets stamina recovery rate
     * @return Recovery rate
     */
    public double getStaminaRecoveryRate() {
        return 0.05;
    }

    /**
     * toString implementation
     */
    @Override
    public String toString() {
        int totalDamage = baseDamage;
        if (inventory != null) {
            totalDamage += inventory.getTotalStatBonus();
        }

        return String.format("Warrior %s [HP: %d/%d, Stamina: %d/%d, Damage: %d, Money: %d, Level: %d]",
                name, health, maxHealth, stamina, maxStamina, totalDamage, money, level);
    }
}