
package factoryMonster;

import iterator.Item;
import logger.GameLogger;
import java.util.logging.Logger;

/**
 * Goblin - Mostro base della Goblin Cave
 */
public class Goblin extends AbstractMonster {
    
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Costruttore per creare un Goblin
     */
    public Goblin() {
        super(
                "Goblin",       // Nome
                "Goblin",       // Tipo
                20,             // Vita
                5,              // Danno
                10,             // Oro
                50              // 50% probabilità di drop
        );
        
        // Aggiunge un possibile drop
        addPossibleDrop(new Item("Pozione di Cura", Item.ItemType.POTION, 15, 0));
        logger.info("Goblin creato con " + health + " HP");
    }
    
    @Override
    public int attack() {
        int damage = super.attack();
        logger.info("Goblin attacca per " + damage + " danni");
        return damage;
    }
    
    @Override
    public String toString() {
        return "Goblin [Vita: " + health + "/" + maxHealth + ", Danno: " + baseDamage + "]";
    }
}