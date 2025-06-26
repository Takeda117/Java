
package java.builder;

import java.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Dungeon - Represents a dungeon in the game
 * <p>
 * This class is the product of the Builder pattern. It represents a dungeon
 * with various properties like name, description, gold reward, and monster type.
 * Instances of this class are created using the DungeonBuilder.
 * </p>
 */
public class Dungeon {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private final String name;
    private final String description;
    private final int goldReward;
    private final String monsterType;
    
    /**
     * Constructor for creating a new Dungeon
     * <p>
     * This constructor is package-private and should only be called by the DungeonBuilder.
     * </p>
     * 
     * @param name The name of the dungeon
     * @param description The description of the dungeon
     * @param goldReward The gold reward for completing the dungeon
     * @param monsterType The type of monster that inhabits the dungeon
     */
    Dungeon(String name, String description, int goldReward, String monsterType) {
        this.name = name;
        this.description = description;
        this.goldReward = goldReward;
        this.monsterType = monsterType;
        logger.info("Dungeon created: " + name + " with monster type " + monsterType);
    }
    
    /**
     * Gets the name of the dungeon
     * 
     * @return The dungeon name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the description of the dungeon
     * 
     * @return The dungeon description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the gold reward for completing the dungeon
     * 
     * @return The gold reward amount
     */
    public int getGoldReward() {
        return goldReward;
    }
    
    /**
     * Gets the type of monster that inhabits the dungeon
     * 
     * @return The monster type
     */
    public String getMonsterType() {
        return monsterType;
    }
    
    /**
     * Returns a string representation of the dungeon
     * 
     * @return The dungeon description
     */
    @Override
    public String toString() {
        return description;
    }
}