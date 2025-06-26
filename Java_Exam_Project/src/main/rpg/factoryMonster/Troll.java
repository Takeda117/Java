package rpg.factoryMonster;

import rpg.iterator.Item;
import rpg.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Troll - Monster of the Swamp of Trolls
 * <p>
 * This class represents a Troll monster type with specific
 * stats and behaviors. Trolls are stronger monsters with
 * high health and damage.
 * </p>
 */
public class Troll extends AbstractMonster {
    
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Constructor for creating a Troll
     */
    public Troll() {
        super(
                "Troll",        // Name
                "Troll",        // Type
                40,             // Health
                8,              // Damage
                20,             // Gold
                50              // 50% drop chance
        );

        // Add possible drops
        addPossibleDrop(new Item("Large Health Potion", Item.ItemType.POTION, 30, 0));
        addPossibleDrop(new Item("Club", Item.ItemType.WEAPON, 50, 3));
        logger.info("Troll created with " + health + " HP");
    }
    
    /**
     * Troll attack implementation
     * 
     * @return The damage dealt by the attack
     */
    @Override
    public int attack() {
        int damage = super.attack();
        logger.info("Troll attacks for " + damage + " damage");
        return damage;
    }
    
    /**
     * Returns a string representation of the Troll
     * 
     * @return Formatted string with Troll stats
     */
    @Override
    public String toString() {
        return "Troll [Health: " + health + "/" + maxHealth + ", Damage: " + baseDamage + "]";
    }
}