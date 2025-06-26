package java.factoryMonster;

import java.iterator.Item;
import java.util.List;

/**
 * Monster - Represents a generic enemy that players fight in dungeons
 * <p>
 * This class extends AbstractMonster to avoid duplication
 * and ensure consistent behavior across all monster types.
 * It provides a simplified implementation for basic monsters.
 * </p>
 */
public class Monster extends AbstractMonster {

    /**
     * Constructor for creating a generic monster
     * 
     * @param name The monster's name
     * @param type The monster's type
     * @param health The monster's health points
     * @param damage The monster's base damage
     * @param goldDrop The gold dropped when defeated
     */
    public Monster(String name, String type, int health, int damage, int goldDrop) {
        // Call the AbstractMonster constructor with a default drop chance of 30%
        super(name, type, health, damage, goldDrop, 30);
    }

    /**
     * Simplified attack method that uses the base implementation
     * 
     * @return The damage dealt by the attack
     */
    @Override
    public int attack() {
        // Use the base implementation from AbstractMonster
        return super.attack();
    }

    /**
     * Take damage from player - uses the base implementation
     * 
     * @param damage The amount of damage to take
     */
    @Override
    public void takeDamage(int damage) {
        // Use the base implementation from AbstractMonster
        super.takeDamage(damage);
    }

    /**
     * Add a possible item drop - uses the base implementation
     * 
     * @param item The item to add as a possible drop
     */
    @Override
    public void addPossibleDrop(Item item) {
        // Use the base implementation from AbstractMonster
        super.addPossibleDrop(item);
    }

    /**
     * Get items dropped when defeated - uses the base implementation
     * Not all items are guaranteed to drop
     * 
     * @return List of items dropped by the monster
     */
    @Override
    public List<Item> getDroppedItems() {
        // Use the base implementation from AbstractMonster
        return super.getDroppedItems();
    }

    /**
     * Simple toString implementation that uses the base implementation
     * 
     * @return String representation of the monster
     */
    @Override
    public String toString() {
        return super.toString();
    }
}