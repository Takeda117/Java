package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.io.CharacterManagement;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * CharacterMenuBuilder - Builds character-specific menu
 * Implements only required functionality without extras
 */
public class CharacterMenuBuilder {

    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Show character menu - this replaces the main menu temporarily
     */
    public static void showCharacterMenu(Character character) {
        if (character == null) {
            logger.warning("Cannot show character menu: null character");
            return;
        }

        // Verifica se il personaggio è vivo prima di mostrare il menu
        if (!character.isAlive()) {
            logger.warning("Character is dead, returning to main menu: " + character.getName());
            System.out.println("Il tuo personaggio è morto! Ritorno al menu principale.");
            throw new ReturnToMainMenuException();
        }

        try {
            GameMenu characterMenu = buildCharacterMenu(character);
            characterMenu.execute();
        } catch (ReturnToMainMenuException e) {
            // Propagate the exception to return to main menu
            throw e;
        } catch (Exception e) {
            logger.severe("Error in character menu: " + e.getMessage());
            System.out.println("Errore nel menu personaggio.");
        }
    }

    /**
     * Build character menu with required options only
     */
    private static GameMenu buildCharacterMenu(Character character) {
        GameMenu menu = new GameMenu("Menu Personaggio - " + character.getName());

        menu.add(new MenuItem("Allenati", () -> trainCharacter(character)));
        menu.add(new MenuItem("Riposa", () -> restCharacter(character)));
        menu.add(new MenuItem("Accedi inventario", () -> InventoryMenuBuilder.showInventoryMenu(character)));
        menu.add(new MenuItem("Esplora Dungeon", () -> DungeonMenuBuilder.showDungeonMenu(character)));
        menu.add(new MenuItem("Salva", () -> saveAndReturnToMain(character)));
        menu.add(new MenuItem("Torna al menu principale", () -> exitToMain()));

        return menu;
    }

    /**
     * Train character - simple: consume stamina, increase base damage
     */
    private static void trainCharacter(Character character) {
        try {
            System.out.println("\n=== ALLENAMENTO ===");
            System.out.println("Personaggio: " + character);

            if (character.getStamina() < 10) {
                System.out.println("Non hai abbastanza stamina per allenarti! (Serve almeno 10)");
                return;
            }

            System.out.print("\nConfermi l'allenamento? (s/n): ");
            if (InputValidator.validateYesNo(scanner.nextLine())) {

                // Simple training: consume stamina, increase damage
                int oldDamage = character.getBaseDamage();
                int oldStamina = character.getStamina();
                
                // Consume stamina - questo metodo è usato per ridurre la stamina
                // Il valore negativo indica una riduzione
                character.restoreStamina(-10);
                
                // Verifica che la stamina sia effettivamente diminuita
                if (character.getStamina() >= oldStamina) {
                    // Se la stamina non è diminuita, forziamo la riduzione
                    // Questo è un fallback nel caso in cui restoreStamina non funzioni come previsto
                    try {
                        // Accesso diretto al campo stamina tramite reflection (soluzione di emergenza)
                        java.lang.reflect.Field staminaField = character.getClass().getSuperclass().getDeclaredField("stamina");
                        staminaField.setAccessible(true);
                        int currentStamina = (int) staminaField.get(character);
                        staminaField.set(character, currentStamina - 10);
                        logger.warning("Used reflection to reduce stamina as normal method failed");
                    } catch (Exception e) {
                        logger.severe("Failed to reduce stamina via reflection: " + e.getMessage());
                    }
                }
                
                character.train(); // This will increase damage and reduce max stamina

                System.out.println("Allenamento completato!");
                System.out.println("Danno aumentato da " + oldDamage + " a " + character.getBaseDamage());
                System.out.println("Stamina consumata: -10 (Stamina attuale: " + character.getStamina() + ")");

                logger.info("Character trained: " + character.getName());
            }
        } catch (Exception e) {
            logger.warning("Error training character: " + e.getMessage());
            System.out.println("Errore durante l'allenamento.");
        }
    }

    /**
     * Rest character - restore health and stamina
     */
    private static void restCharacter(Character character) {
        try {
            System.out.println("\n=== RIPOSO ===");
            System.out.println("Stato attuale: " + character);

            if (character.getHealth() == character.getMaxHealth() &&
                    character.getStamina() == character.getMaxStamina()) {
                System.out.println("Sei già completamente riposato!");
                return;
            }

            System.out.print("\nVuoi riposare? (s/n): ");
            if (InputValidator.validateYesNo(scanner.nextLine())) {

                int oldHealth = character.getHealth();
                int oldStamina = character.getStamina();

                character.rest(); // Full restore

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
        } catch (Exception e) {
            logger.warning("Error resting character: " + e.getMessage());
            System.out.println("Errore durante il riposo.");
        }
    }

    /**
     * Save character and return to main menu
     */
    private static void saveAndReturnToMain(Character character) {
        try {
            System.out.println("\n=== SALVATAGGIO ===");
            System.out.print("Nome del salvataggio: ");
            String filename = InputValidator.sanitizeInput(scanner.nextLine());

            if (!filename.isEmpty()) {
                boolean success = CharacterManagement.saveCharacter(character, filename);
                if (success) {
                    System.out.println("Personaggio salvato!");
                    logger.info("Character saved: " + character.getName());

                    // Force return to main menu by throwing exception
                    // This will exit the character menu loop
                    throw new ReturnToMainMenuException();
                } else {
                    System.out.println("Errore nel salvataggio!");
                }
            } else {
                System.out.println("Nome non valido!");
            }
        } catch (ReturnToMainMenuException e) {
            // This is expected - we want to return to main menu
            throw e;
        } catch (Exception e) {
            logger.warning("Error saving character: " + e.getMessage());
            System.out.println("Errore nel salvataggio.");
        }
    }

    /**
     * Exit to main menu
     */
    private static void exitToMain() {
        logger.info("User returned to main menu");
        throw new ReturnToMainMenuException();
    }

    /**
     * Custom exception to signal return to main menu
     */
    public static class ReturnToMainMenuException extends RuntimeException {
        // Used to break out of character menu and return to main menu
    }
}
