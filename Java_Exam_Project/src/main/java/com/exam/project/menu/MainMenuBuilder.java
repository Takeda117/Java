package com.exam.project.menu;

import com.exam.project.composite.*;
import com.exam.project.factory.Character;
import com.exam.project.factory.CharacterFactory;
import com.exam.project.io.CharacterManagement;
import com.exam.project.security.InputValidator;
import com.exam.project.logger.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * MainMenuBuilder - Builds the main game menu
 * Implements only the required functionality: Create, Load, Exit
 */
public class MainMenuBuilder {

    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Build main menu with only required options
     */
    public static GameMenu buildMainMenu() {
        try {
            GameMenu mainMenu = new GameMenu("RPG Adventure Game - Menu Principale");

            mainMenu.add(new MenuItem("Crea nuovo personaggio", MainMenuBuilder::createNewCharacter));
            mainMenu.add(new MenuItem("Carica Personaggio", MainMenuBuilder::loadCharacter));
            mainMenu.add(new MenuItem("Esci", MainMenuBuilder::exitGame));

            return mainMenu;
        } catch (Exception e) {
            logger.severe("Error building main menu: " + e.getMessage());
            // Emergency fallback
            GameMenu emergency = new GameMenu("Emergency Menu");
            emergency.add(new MenuItem("Esci", () -> System.exit(0)));
            return emergency;
        }
    }

    /**
     * Create new character and go to character menu
     */
    private static void createNewCharacter() {
        try {
            System.out.println("\n=== CREA NUOVO PERSONAGGIO ===");

            CharacterFactory factory = new CharacterFactory();
            factory.showAvailableTypes();

            System.out.print("\nTipo (warrior/mage): ");
            String type = InputValidator.sanitizeInput(scanner.nextLine());

            System.out.print("Nome: ");
            String name = InputValidator.validateCharacterName(scanner.nextLine());

            if (name == null) {
                return;
            }

            Character character = factory.createCharacter(type, name);

            if (character != null) {
                System.out.println("Personaggio creato: " + character.getName());
                logger.info("Character created: " + character.getName());

                // Go to character menu
                CharacterMenuBuilder.showCharacterMenu(character);
            } else {
                System.out.println("Creazione fallita!");
            }
        } catch (Exception e) {
            logger.warning("Error creating character: " + e.getMessage());
            System.out.println("Errore nella creazione del personaggio.");
        }
    }

    /**
     * Load existing character and go to character menu
     */
    private static void loadCharacter() {
        try {
            System.out.println("\n=== CARICA PERSONAGGIO ===");

            String[] saves = CharacterManagement.listSaveFiles();
            if (saves.length == 0) {
                System.out.println("Nessun salvataggio trovato!");
                return;
            }

            System.out.println("Salvataggi disponibili:");
            for (int i = 0; i < saves.length; i++) {
                System.out.println((i + 1) + ". " + saves[i]);
            }

            System.out.print("\nScegli (1-" + saves.length + "): ");
            Integer choice = InputValidator.validateMenuChoice(scanner.nextLine(), saves.length);

            if (choice != null && choice > 0) {
                Character loaded = CharacterManagement.loadCharacter(saves[choice - 1]);
                if (loaded != null) {
                    System.out.println("Personaggio caricato: " + loaded.getName());

                    // Go to character menu
                    CharacterMenuBuilder.showCharacterMenu(loaded);
                }
            }
        } catch (Exception e) {
            logger.warning("Error loading character: " + e.getMessage());
            System.out.println("Errore nel caricamento.");
        }
    }

    /**
     * Exit the game
     */
    private static void exitGame() {
        System.out.println("\nGrazie per aver giocato!");
        logger.info("Game exited by user");
        System.exit(0);
    }
}
