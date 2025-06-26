
package rpg.factoryMonster;

import rpg.iterator.Item;
import rpg.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Goblin - Basic monster of the Goblin Cave
 * <p>
 * This class represents a Goblin monster type with specific
 * stats and behaviors. Goblins are weaker monsters with
 * moderate health and damage.
 * </p>
 */
public class Goblin extends AbstractMonster {
    
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Constructor for creating a Goblin
     */
    public Goblin() {
        super(
                "Goblin",       // Name
                "Goblin",       // Type
                20,             // Health
                5,              // Damage
                10,             // Gold
                50              // 50% drop chance
        );
        
        // Add a possible drop
        addPossibleDrop(new Item("Health Potion", Item.ItemType.POTION, 15, 0));
        logger.info("Goblin created with " + health + " HP");
    }
    
    /**
     * Goblin attack implementation
     * 
     * @return The damage dealt by the attack
     */
    @Override
    public int attack() {
        int damage = super.attack();
        logger.info("Goblin attacks for " + damage + " damage");
        return damage;
    }
    
    /**
     * Returns a string representation of the Goblin
     * 
     * @return Formatted string with Goblin stats
     */
    @Override
    public String toString() {
        return "Goblin [Health: " + health + "/" + maxHealth + ", Damage: " + baseDamage + "]";
    }
}