package builder;

import logger.GameLogger;
import java.util.logging.Logger;

/**
 * ConcreteDungeonBuilder - Concrete implementation of the DungeonBuilder interface
 * <p>
 * This class implements the Builder pattern for creating Dungeon objects.
 * It provides methods to set various attributes of a dungeon and finally build it.
 * Each setter method returns the builder instance to allow method chaining.
 * </p>
 */
public class ConcreteDungeonBuilder implements DungeonBuilder {
    
    private static final Logger logger = GameLogger.getLogger();
    
    private String name;
    private String description;
    private int goldReward;
    private String monsterType;
    
    /**
     * Resets the builder to its default state
     * 
     * @return The builder instance for method chaining
     */
    @Override
    public DungeonBuilder reset() {
        this.name = "Dungeon";
        this.description = "A mysterious dungeon";
        this.goldReward = 50;
        this.monsterType = "goblin";
        logger.fine("DungeonBuilder reset");
        return this;
    }
    
    /**
     * Sets the name of the dungeon
     * 
     * @param name The name to set for the dungeon
     * @return The builder instance for method chaining
     */
    @Override
    public DungeonBuilder setName(String name) {
        this.name = name;
        logger.fine("Dungeon name set: " + name);
        return this;
    }
    
    /**
     * Sets the description of the dungeon
     * 
     * @param description The description to set for the dungeon
     * @return The builder instance for method chaining
     */
    @Override
    public DungeonBuilder setDescription(String description) {
        this.description = description;
        logger.fine("Dungeon description set");
        return this;
    }
    
    /**
     * Sets the gold reward for completing the dungeon
     * 
     * @param goldReward The amount of gold to reward
     * @return The builder instance for method chaining
     */
    @Override
    public DungeonBuilder setGoldReward(int goldReward) {
        this.goldReward = goldReward;
        logger.fine("Dungeon reward set: " + goldReward);
        return this;
    }
    
    /**
     * Sets the type of monster that inhabits the dungeon
     * 
     * @param monsterType The type of monster to set
     * @return The builder instance for method chaining
     */
    @Override
    public DungeonBuilder setMonsterType(String monsterType) {
        this.monsterType = monsterType;
        logger.fine("Dungeon monster type set: " + monsterType);
        return this;
    }
    
    /**
     * Builds and returns a new Dungeon instance with the configured properties
     * 
     * @return A new Dungeon instance
     */
    @Override
    public Dungeon build() {
        logger.info("Building dungeon: " + name);
        return new Dungeon(name, description, goldReward, monsterType);
    }
}