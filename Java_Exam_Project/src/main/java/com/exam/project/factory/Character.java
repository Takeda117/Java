package com.exam.project.factory;

import com.exam.project.iterator.Item;

/**
 * Character interface - defines what every character can do
 * Part of the Factory Pattern implementation
 */
public interface Character {

    // Combat Actions
    int attack();
    void takeDamage(int damage);
    boolean isAlive();
    
    // Character Management
    void train();
    void rest();
    void restoreStamina(int amount);
    
    // Inventory Management
    void equipItem(Item item);
    void addItem(Item item);
    void showInventory();
    
    // Getters
    String getName();
    int getHealth();
    int getMaxHealth();
    int getStamina();
    int getMaxStamina();
    int getBaseDamage();
    int getMoney();
    int getLevel();
}