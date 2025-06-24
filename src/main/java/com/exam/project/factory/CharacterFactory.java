package com.exam.project.factory;

/**
 * CharacterFactory - Creates different types of characters
 * Simple Factory Pattern implementation
 */
public class CharacterFactory {

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
}