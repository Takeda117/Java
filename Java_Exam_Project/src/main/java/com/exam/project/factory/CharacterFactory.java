package com.exam.project.factory;

/**
 * Factory for creating character objects
 * Implementa il Factory Method Pattern
 */
public class CharacterFactory {

    /**
     * Create a character based on type
     * @param characterType Type of character to create
     * @param name Name of the character
     * @return A new character instance
     */
    public Character createCharacter(String characterType, String name) {
        if (characterType == null || name == null) {
            return null;
        }

        // Clean the inputs
        String safeInputName = name.trim();
        String safeInputType = characterType.toLowerCase().trim();

        // Create the right character type
        switch (safeInputType) {
            case "warrior", "w":
                return new Warrior(safeInputName);

            case "mage", "m":
                return new Mage(safeInputName);

            default:
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
}
