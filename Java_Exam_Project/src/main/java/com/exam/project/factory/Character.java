package com.exam.project.factory;

import com.exam.project.iterator.Item;

/**
 * Character interface - defines what every character can do
 * Part of the Factory Pattern implementation
 */
public interface Character {

    // Core combat actions
    int attack();  // Returns damage dealt
    void takeDamage(int damage);
    boolean isAlive();

    // Character progression
    void train();  // Increases stats but has stamina cost

    // Basic character info
    String getName();
    int getHealth();
    int getMaxHealth();
    int getStamina();
    int getMaxStamina();
    int getBaseDamage();
    int getMoney();
    int getLevel();

    // Recovery system
    void restoreStamina(int amount);
    void rest();  // Full health and stamina recovery

    void equipItem(Item sword);
    void addItem(Item sword);

    void showInventory();
}