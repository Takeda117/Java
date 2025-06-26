package factoryMonster;

import iterator.Item;
import java.util.List;


/**
 * Monster - Represents an enemy that players fight in dungeons
 *
 * This class now properly extends AbstractMonster to avoid duplication
 * and ensure consistent behavior across all monster types.
 */
public class Monster extends AbstractMonster {

    /**
     * Constructor for creating a monster
     */
    public Monster(String name, String type, int health, int damage, int goldDrop) {
        // Call the AbstractMonster constructor with a default drop chance of 30%
        super(name, type, health, damage, goldDrop, 30);
    }

    /**
     * Simplified attack method that uses the base implementation
     */
    @Override
    public int attack() {
        // Use the base implementation from AbstractMonster
        return super.attack();
    }

    /**
     * Take damage from player - uses the base implementation
     */
    @Override
    public void takeDamage(int damage) {
        // Use the base implementation from AbstractMonster
        super.takeDamage(damage);
    }

    /**
     * Add a possible item drop - uses the base implementation
     */
    @Override
    public void addPossibleDrop(Item item) {
        // Use the base implementation from AbstractMonster
        super.addPossibleDrop(item);
    }

    /**
     * Get items dropped when defeated - uses the base implementation
     * Not all items are guaranteed to drop
     */
    @Override
    public List<Item> getDroppedItems() {
        // Use the base implementation from AbstractMonster
        return super.getDroppedItems();
    }

    /**
     * Simple toString implementation that uses the base implementation
     */
    @Override
    public String toString() {
        return super.toString();
    }
}