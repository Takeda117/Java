package java.factory;

import java.rpgSecurity.InputValidator;
import java.util.logging.Logger;

/**
 * Factory for creating character objects
 * <p>
 * This class implements the Factory Method Pattern, providing methods
 * to create different types of character objects. It encapsulates the
 * instantiation logic and provides a common interface for creating
 * various character types.
 * </p>
 */
public class CharacterFactory {

    private static final Logger logger = Logger.getLogger(CharacterFactory.class.getName());

    /**
     * Creates a character based on the specified type
     * 
     * @param type The type of character to create ("warrior" or "mage")
     * @param name The name for the character
     * @return A new Character instance of the specified type, or null if creation fails
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
     * Shows available character types to the user
     */
    public void showAvailableTypes() {
        System.out.println("Available character types:");
        System.out.println("- warrior: Strong fighter with high health");
        System.out.println("- mage: Magic user with spells");
    }
    
    /**
     * Creates a custom character with specific attribute values
     * 
     * @param type The type of character to create
     * @param name The name for the character
     * @param health The initial health value
     * @param maxHealth The maximum health value
     * @param stamina The initial stamina value
     * @param maxStamina The maximum stamina value
     * @param baseDamage The base damage value
     * @param money The initial money amount
     * @param level The character level
     * @return A new Character instance with custom attributes, or null if creation fails
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
     * Creates a custom mage with specific attribute values including mana
     * 
     * @param name The name for the mage
     * @param health The initial health value
     * @param maxHealth The maximum health value
     * @param stamina The initial stamina value
     * @param maxStamina The maximum stamina value
     * @param baseDamage The base damage value
     * @param money The initial money amount
     * @param level The character level
     * @param mana The initial mana value
     * @param maxMana The maximum mana value
     * @return A new Mage instance with custom attributes, or null if creation fails
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
     * 
     * @param character The character to customize
     * @param health The health value to set
     * @param maxHealth The maximum health value to set
     * @param stamina The stamina value to set
     * @param maxStamina The maximum stamina value to set
     * @param baseDamage The base damage value to set
     * @param money The money amount to set
     * @param level The level to set
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