package rpg.builder;

/**
 * DungeonBuilder - Interface for the Builder Pattern implementation
 * <p>
 * This interface defines the contract for creating dungeons using the Builder pattern.
 * It provides methods to set various attributes of a dungeon and finally build it.
 * </p>
 */
public interface DungeonBuilder {
    
    /**
     * Resets the builder to its default state
     * 
     * @return The builder instance for method chaining
     */
    DungeonBuilder reset();
    
    /**
     * Sets the name of the dungeon
     * 
     * @param name The name to set for the dungeon
     * @return The builder instance for method chaining
     */
    DungeonBuilder setName(String name);
    
    /**
     * Sets the description of the dungeon
     * 
     * @param description The description to set for the dungeon
     * @return The builder instance for method chaining
     */
    DungeonBuilder setDescription(String description);
    
    /**
     * Sets the gold reward for completing the dungeon
     * 
     * @param goldReward The amount of gold to reward
     * @return The builder instance for method chaining
     */
    DungeonBuilder setGoldReward(int goldReward);
    
    /**
     * Sets the type of monster that inhabits the dungeon
     * 
     * @param monsterType The type of monster to set
     * @return The builder instance for method chaining
     */
    DungeonBuilder setMonsterType(String monsterType);
    
    /**
     * Builds and returns a new Dungeon instance with the configured properties
     * 
     * @return A new Dungeon instance
     */
    Dungeon build();
}
