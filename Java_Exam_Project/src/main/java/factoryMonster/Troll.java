package factoryMonster;

import iterator.Item;
import logger.GameLogger;
import java.util.logging.Logger;

/**
 * Troll - Mostro della Swamp of Trolls
 */
public class Troll extends AbstractMonster {
    
    private static final Logger logger = GameLogger.getLogger();

    /**
     * Costruttore per creare un Troll
     */
    public Troll() {
        super(
                "Troll",        // Nome
                "Troll",        // Tipo
                40,             // Vita
                8,              // Danno
                20,             // Oro
                50              // 50% probabilità di drop
        );

        // Aggiunge un possibile drop
        addPossibleDrop(new Item("Pozione di Cura Grande", Item.ItemType.POTION, 30, 0));
        addPossibleDrop(new Item("Mazza", Item.ItemType.WEAPON, 50, 3));
        logger.info("Troll creato con " + health + " HP");
    }
    
    @Override
    public int attack() {
        int damage = super.attack();
        logger.info("Troll attacca per " + damage + " danni");
        return damage;
    }
    
    @Override
    public String toString() {
        return "Troll [Vita: " + health + "/" + maxHealth + ", Danno: " + baseDamage + "]";
    }
}