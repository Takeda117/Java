package com.exam.project.factoryMonster;

import com.exam.project.iterator.Item;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Monster - Represents an enemy that players fight in dungeons
 *
 * Monsters have simpler stats than characters but can drop items
 */
public class Monster {

    private String name;
    private String type;
    private int health;
    private int maxHealth;
    private int damage;
    private int goldDrop;
    private List<Item> possibleDrops;
    private Random random = new Random();

    /**
     * Constructor for creating a monster
     */
    public Monster(String name, String type, int health, int damage, int goldDrop) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.goldDrop = goldDrop;
        this.possibleDrops = new ArrayList<>();
    }

    /**
     * Monster attacks - simpler than character attack
     */
    public int attack() {
        // Monsters have some variance in their damage
        int variance = random.nextInt(3) - 1; // -1 to +1
        int totalDamage = damage + variance;

        System.out.printf("%s attacks for %d damage!%n", name, totalDamage);
        return Math.max(1, totalDamage); // At least 1 damage
    }

    /**
     * Take damage from player
     */
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
     * Check if monster is still alive
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Add a possible item drop
     */
    public void addPossibleDrop(Item item) {
        if (item != null) {
            possibleDrops.add(item);
        }
    }

    /**
     * Get items dropped when defeated
     * Not all items are guaranteed to drop
     */
    public List<Item> getDroppedItems() {
        List<Item> drops = new ArrayList<>();

        for (Item item : possibleDrops) {
            // 30% chance to drop each item
            if (random.nextInt(100) < 30) {
                drops.add(item);
            }
        }

        return drops;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    @Override
    public String toString() {
        return String.format("%s %s [HP: %d/%d, Damage: %d, Gold: %d]",
                type, name, health, maxHealth, damage, goldDrop);
    }
}
