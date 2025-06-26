package com.exam.project.factory;

import com.exam.project.security.InputValidator;
import java.util.logging.Logger;

/**
 * Factory for creating character objects
 * Implementa il Factory Method Pattern
 */
public class CharacterFactory {

    private static final Logger logger = Logger.getLogger(CharacterFactory.class.getName());

    /**
     * Create a character based on type
     */
    public Character createCharacter(String type, String name) {
        logger.info("Creating character of type: " + type + " with name: " + name);
        
        // Validate name
        String validatedName = InputValidator.validateCharacterName(name);
        if (validatedName == null) {
            logger.warning("Character creation failed: invalid name");
            return null;
        }
        
        // Sanitize and validate type
        String sanitizedType = InputValidator.sanitizeInput(type);
        if (sanitizedType.isEmpty()) {
            logger.warning("Character creation failed: empty type");
            System.out.println("Character type cannot be empty!");
            return null;
        }
        
        // Convert to lowercase for case-insensitive comparison
        sanitizedType = sanitizedType.toLowerCase();
        
        // Create character based on type
        try {
            if (sanitizedType.equals("warrior")) {
                return new Warrior(validatedName);
            } else if (sanitizedType.equals("mage")) {
                return new Mage(validatedName);
            } else {
                logger.warning("Character creation failed: invalid type: " + sanitizedType);
                System.out.println("Invalid character type!");
                return null;
            }
        } catch (Exception e) {
            logger.severe("Error creating character: " + e.getMessage());
            System.out.println("Error creating character: " + e.getMessage());
            return null;
        }
    }

    /**
     * Shows available character types
     */
    public void showAvailableTypes() {
        System.out.println("Available character types:");
        System.out.println("- warrior: Strong fighter with high health");
        System.out.println("- mage: Magic user with spells");
    }
    
    /**
     * Crea un personaggio personalizzato con valori specifici
     */
    public Character createCustomCharacter(String type, String name, int health, int maxHealth, 
                                          int stamina, int maxStamina, int baseDamage, int money, int level) {
        // Validate inputs
        String validatedName = InputValidator.validateCharacterName(name);
        if (validatedName == null) {
            logger.warning("Custom character creation failed: invalid name");
            return null;
        }
        
        try {
            // Create character with custom values
            if (type.equalsIgnoreCase("warrior")) {
                Warrior warrior = new Warrior(validatedName);
                customizeCharacter(warrior, health, maxHealth, stamina, maxStamina, baseDamage, money, level);
                return warrior;
            } else if (type.equalsIgnoreCase("mage")) {
                Mage mage = new Mage(validatedName);
                customizeCharacter(mage, health, maxHealth, stamina, maxStamina, baseDamage, money, level);
                return mage;
            } else {
                logger.warning("Custom character creation failed: invalid type: " + type);
                return null;
            }
        } catch (Exception e) {
            logger.warning("Error creating custom character: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Crea un mago personalizzato con valori specifici
     */
    public Character createCustomMage(String name, int health, int maxHealth, int stamina, int maxStamina,
                                     int baseDamage, int money, int level, int mana, int maxMana) {
        // Validate name
        String validatedName = InputValidator.validateCharacterName(name);
        if (validatedName == null) {
            logger.warning("Custom mage creation failed: invalid name");
            return null;
        }
        
        try {
            // Create and customize mage
            Mage mage = new Mage(validatedName);
            customizeCharacter(mage, health, maxHealth, stamina, maxStamina, baseDamage, money, level);
            
            // Set mage-specific values
            mage.mana = Math.max(0, mana);
            mage.maxMana = Math.max(1, maxMana);
            
            return mage;
        } catch (Exception e) {
            logger.warning("Error creating custom mage: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to customize character attributes
     */
    private void customizeCharacter(AbstractCharacter character, int health, int maxHealth, 
                                   int stamina, int maxStamina, int baseDamage, int money, int level) {
        // Set values directly (safe because we're in the same package)
        character.health = Math.max(0, health);
        character.maxHealth = Math.max(1, maxHealth);
        character.stamina = Math.max(0, stamina);
        character.maxStamina = Math.max(1, maxStamina);
        character.baseDamage = Math.max(1, baseDamage);
        character.money = Math.max(0, money);
        character.level = Math.max(1, level);
    }
}