package java.factory;

import java.iterator.Item;

/**
 * Character interface - defines what every character can do
 * <p>
 * This interface is part of the Factory Pattern implementation.
 * It defines the contract that all character types must implement,
 * providing a common interface for client code to work with different
 * character types created by the CharacterFactory.
 * </p>
 */
public interface Character {

    /**
     * Performs an attack and calculates damage
     * 
     * @return The amount of damage dealt by the attack
     */
    int attack();
    
    /**
     * Takes damage from an attack
     * 
     * @param damage The amount of damage to take
     */
    void takeDamage(int damage);
    
    /**
     * Checks if the character is still alive
     * 
     * @return true if the character has health remaining, false otherwise
     */
    boolean isAlive();
    
    /**
     * Trains the character to improve stats
     */
    void train();
    
    /**
     * Rests to recover stamina and possibly health
     */
    void rest();
    
    /**
     * Restores or reduces stamina by the specified amount
     * 
     * @param amount The amount of stamina to restore (positive) or reduce (negative)
     */
    void restoreStamina(int amount);
    
    /**
     * Equips an item to the character
     * 
     * @param item The item to equip
     */
    void equipItem(Item item);
    
    /**
     * Adds an item to the character's inventory
     * 
     * @param item The item to add
     */
    void addItem(Item item);
    
    /**
     * Displays the character's inventory contents
     */
    void showInventory();
    
    /**
     * Gets the character's name
     * 
     * @return The character's name
     */
    String getName();
    
    /**
     * Gets the character's current health
     * 
     * @return The current health value
     */
    int getHealth();
    
    /**
     * Gets the character's maximum health
     * 
     * @return The maximum health value
     */
    int getMaxHealth();
    
    /**
     * Gets the character's current stamina
     * 
     * @return The current stamina value
     */
    int getStamina();
    
    /**
     * Gets the character's maximum stamina
     * 
     * @return The maximum stamina value
     */
    int getMaxStamina();
    
    /**
     * Gets the character's base damage
     * 
     * @return The base damage value
     */
    int getBaseDamage();
    
    /**
     * Gets the character's money
     * 
     * @return The amount of money
     */
    int getMoney();
    
    /**
     * Gets the character's level
     * 
     * @return The character's level
     */
    int getLevel();
}