package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.io.CharacterManagement;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * CharacterMenu - Manages character-specific menu
 */
public class CharacterMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Shows character menu
     */
    public static void showCharacterMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show character menu: null character");
            return;
        }

        if (!character.isAlive()) {
            logger.warning("Character is dead, returning to main menu: " + character.getName());
            System.out.println("Il tuo personaggio è morto! Ritorno al menu principale.");
            throw new ReturnToMainMenuException();
        }

        try {
            GameMenu characterMenu = buildCharacterMenu(character);
            characterMenu.execute();
        } catch (ReturnToMainMenuException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error in character menu: " + e.getMessage());
            System.out.println("Errore nel menu personaggio.");
        }
    }

    /**
     * Builds character menu
     */
    private static GameMenu buildCharacterMenu(Character character) {
        GameMenu menu = new GameMenu("Menu Personaggio - " + character.getName());
        menu.add(new MenuItem("Allenati", () -> trainCharacter(character)));
        menu.add(new MenuItem("Riposa", () -> restCharacter(character)));
        menu.add(new MenuItem("Accedi inventario", () -> InventoryMenu.showInventoryMenu(character)));
        menu.add(new MenuItem("Esplora Dungeon", () -> DungeonMenu.showDungeonMenu(character)));
        menu.add(new MenuItem("Salva", () -> saveAndReturnToMain(character)));
        menu.add(new MenuItem("Torna al menu principale", CharacterMenu::exitToMain));
        return menu;
    }

    /**
     * Trains character
     */
    private static void trainCharacter(Character character) {
        System.out.println("\n=== ALLENAMENTO ===");
        System.out.println("Personaggio: " + character);

        if (character.getStamina() < 10) {
            System.out.println("Non hai abbastanza stamina per allenarti! (Serve almeno 10)");
            return;
        }

        int oldDamage = character.getBaseDamage();
        int oldStamina = character.getStamina();
        
        character.restoreStamina(-10);
        
        if (character.getStamina() >= oldStamina) {
            reduceStaminaViaReflection(character);
        }
        
        character.train();

        System.out.println("Allenamento completato!");
        System.out.println("Danno aumentato da " + oldDamage + " a " + character.getBaseDamage());
        System.out.println("Stamina consumata: -10 (Stamina attuale: " + character.getStamina() + ")");

        logger.info("Character trained: " + character.getName());
    }

    /**
     * Reduces stamina using reflection as a fallback method
     */
    private static void reduceStaminaViaReflection(Character character) {
        try {
            java.lang.reflect.Field staminaField = character.getClass().getSuperclass().getDeclaredField("stamina");
            staminaField.setAccessible(true);
            int currentStamina = (int) staminaField.get(character);
            staminaField.set(character, currentStamina - 10);
            logger.warning("Used reflection to reduce stamina as normal method failed");
        } catch (Exception e) {
            logger.severe("Failed to reduce stamina via reflection: " + e.getMessage());
        }
    }

    /**
     * Rests character
     */
    private static void restCharacter(Character character) {
        System.out.println("\n=== RIPOSO ===");
        System.out.println("Stato attuale: " + character);

        if (character.getHealth() == character.getMaxHealth() &&
                character.getStamina() == character.getMaxStamina()) {
            System.out.println("Sei già completamente riposato!");
            return;
        }

        int oldHealth = character.getHealth();
        int oldStamina = character.getStamina();

        character.rest();

        int healthRecovered = character.getHealth() - oldHealth;
        int staminaRecovered = character.getStamina() - oldStamina;

        System.out.println("Riposo completato!");
        if (healthRecovered > 0) {
            System.out.println("Vita recuperata: +" + healthRecovered);
        }
        if (staminaRecovered > 0) {
            System.out.println("Stamina recuperata: +" + staminaRecovered);
        }

        logger.info("Character rested: " + character.getName());
    }

    /**
     * Saves character and returns to main menu
     */
    private static void saveAndReturnToMain(Character character) {
        System.out.println("\n=== SALVATAGGIO ===");
        System.out.print("Nome del salvataggio: ");
        String filename = InputValidator.sanitizeInput(scanner.nextLine());

        if (!filename.isEmpty()) {
            boolean success = CharacterManagement.saveCharacter(character, filename);
            if (success) {
                System.out.println("Personaggio salvato!");
                logger.info("Character saved: " + character.getName());
                throw new ReturnToMainMenuException();
            } else {
                System.out.println("Errore nel salvataggio!");
            }
        } else {
            System.out.println("Nome non valido!");
        }
    }

    /**
     * Exits to main menu
     */
    private static void exitToMain() {
        logger.info("User returned to main menu");
        throw new ReturnToMainMenuException();
    }

    /**
     * Custom exception to signal return to main menu
     */
    public static class ReturnToMainMenuException extends RuntimeException {
    }
}