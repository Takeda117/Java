package com.exam.project.factory;

import java.util.logging.Logger;

/**
 * CharacterFactory - Creates different types of characters
 * Simple Factory Pattern implementation
 */
public class CharacterFactory {

    private static final Logger logger = Logger.getLogger(CharacterFactory.class.getName());

    /**
     * Creates a character based on type and name
     */
    public Character createCharacter(String characterType, String name) {
        // Basic input checks
        if (characterType == null || name == null) {
            throw new IllegalArgumentException("Type and name cannot be null");
        }

        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        // Clean the inputs
        String safeInputName = name.trim();
        String safeInputType = characterType.toLowerCase().trim();

        // Create the right character type
        switch (safeInputType) {
            case "warrior", "w":
                System.out.printf("Creating warrior: %s%n", safeInputName);
                return new Warrior(safeInputName);

            case "mage", "m":
                System.out.printf("Creating mage: %s%n", safeInputName);
                return new Mage(safeInputName);

            default:
                throw new IllegalArgumentException(
                        String.format("Unknown type: %s. Use 'warrior' or 'mage'", characterType));
        }
    }

    /**
     * Shows available character types
     */
    public void showAvailableTypes() {
        System.out.println("Available character types:");
        System.out.println("- warrior (w): Strong fighter with high health");
        System.out.println("- mage (m): Magic user with spells");
    }

    /**
     * Crea un personaggio personalizzato con valori specifici
     */
    public Character createCustomCharacter(String type, String name, int health, int maxHealth, 
                                          int stamina, int maxStamina, int baseDamage, int money, int level) {
        logger.info("Creating custom character: " + type + ", " + name);
        
        try {
            Character character = createCharacter(type, name);
            
            if (character == null) {
                return null;
            }
            
            // Usa reflection per impostare i valori
            Class<?> clazz = AbstractCharacter.class;
            setFieldValue(character, clazz, "health", health);
            setFieldValue(character, clazz, "maxHealth", maxHealth);
            setFieldValue(character, clazz, "stamina", stamina);
            setFieldValue(character, clazz, "maxStamina", maxStamina);
            setFieldValue(character, clazz, "baseDamage", baseDamage);
            setFieldValue(character, clazz, "money", money);
            setFieldValue(character, clazz, "level", level);
            
            return character;
        } catch (Exception e) {
            logger.severe("Error creating custom character: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un mago personalizzato con valori specifici incluso mana
     */
    public Character createCustomMage(String name, int health, int maxHealth, int stamina, int maxStamina,
                                     int baseDamage, int money, int level, int mana, int maxMana) {
        logger.info("Creating custom mage: " + name);
        
        try {
            Character character = createCharacter("mage", name);
            
            if (character == null) {
                return null;
            }
            
            // Imposta i valori base
            Class<?> baseClass = AbstractCharacter.class;
            setFieldValue(character, baseClass, "health", health);
            setFieldValue(character, baseClass, "maxHealth", maxHealth);
            setFieldValue(character, baseClass, "stamina", stamina);
            setFieldValue(character, baseClass, "maxStamina", maxStamina);
            setFieldValue(character, baseClass, "baseDamage", baseDamage);
            setFieldValue(character, baseClass, "money", money);
            setFieldValue(character, baseClass, "level", level);
            
            // Imposta i valori specifici del mago
            Class<?> mageClass = Mage.class;
            setFieldValue(character, mageClass, "mana", mana);
            setFieldValue(character, mageClass, "maxMana", maxMana);
            
            return character;
        } catch (Exception e) {
            logger.severe("Error creating custom mage: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method per impostare un valore di campo usando reflection
     */
    private void setFieldValue(Object object, Class<?> clazz, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            logger.warning("Failed to set field " + fieldName + ": " + e.getMessage());
        }
    }
}
