package com.exam.project.factory;

import com.exam.project.security.InputValidator;

import java.lang.reflect.Field;
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
        if (sanitizedType.equals("warrior")) {
            return new Warrior(validatedName);
        } else if (sanitizedType.equals("mage")) {
            return new Mage(validatedName);
        } else {
            logger.warning("Character creation failed: invalid type: " + sanitizedType);
            System.out.println("Invalid character type!");
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
        Character character = createCharacter(type, name);
        
        if (character == null) {
            return null;
        }
        
        // Usa reflection per impostare i valori
        try {
            Class<?> clazz = AbstractCharacter.class;
            setFieldValue(character, clazz, "health", health);
            setFieldValue(character, clazz, "maxHealth", maxHealth);
            setFieldValue(character, clazz, "stamina", stamina);
            setFieldValue(character, clazz, "maxStamina", maxStamina);
            setFieldValue(character, clazz, "baseDamage", baseDamage);
            setFieldValue(character, clazz, "money", money);
            setFieldValue(character, clazz, "level", level);
        } catch (Exception e) {
            // Ignora errori di reflection
        }
        
        return character;
    }
    
    /**
     * Crea un mago personalizzato con valori specifici
     */
    public Character createCustomMage(String name, int health, int maxHealth, int stamina, int maxStamina,
                                     int baseDamage, int money, int level, int mana, int maxMana) {
        Character character = createCharacter("mage", name);
        
        if (character == null) {
            return null;
        }
        
        // Usa reflection per impostare i valori
        try {
            Class<?> abstractClass = AbstractCharacter.class;
            setFieldValue(character, abstractClass, "health", health);
            setFieldValue(character, abstractClass, "maxHealth", maxHealth);
            setFieldValue(character, abstractClass, "stamina", stamina);
            setFieldValue(character, abstractClass, "maxStamina", maxStamina);
            setFieldValue(character, abstractClass, "baseDamage", baseDamage);
            setFieldValue(character, abstractClass, "money", money);
            setFieldValue(character, abstractClass, "level", level);
            
            // Imposta i valori specifici del mago
            Class<?> mageClass = Mage.class;
            setFieldValue(character, mageClass, "mana", mana);
            setFieldValue(character, mageClass, "maxMana", maxMana);
        } catch (Exception e) {
            // Ignora errori di reflection
        }
        
        return character;
    }
    
    /**
     * Utility method per impostare un campo tramite reflection
     */
    private void setFieldValue(Object obj, Class<?> clazz, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}