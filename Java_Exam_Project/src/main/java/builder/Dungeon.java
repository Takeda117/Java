
package builder;

import logger.GameLogger;
import java.util.logging.Logger;

/**
 * Dungeon - Rappresenta un dungeon nel gioco
 */
public class Dungeon {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private final String name;
    private final String description;
    private final int goldReward;
    private final String monsterType;
    
    /**
     * Costruttore
     */
    Dungeon(String name, String description, int goldReward, String monsterType) {
        this.name = name;
        this.description = description;
        this.goldReward = goldReward;
        this.monsterType = monsterType;
        logger.info("Dungeon creato: " + name + " con mostri di tipo " + monsterType);
    }
    
    /**
     * Restituisce il nome del dungeon
     */
    public String getName() {
        return name;
    }
    
    /**
     * Restituisce la descrizione del dungeon
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Restituisce la ricompensa in oro del dungeon
     */
    public int getGoldReward() {
        return goldReward;
    }
    
    /**
     * Restituisce il tipo di mostro del dungeon
     */
    public String getMonsterType() {
        return monsterType;
    }
    
    /**
     * Restituisce una descrizione completa del dungeon
     */
    public String toString() {
        return description;
    }
}