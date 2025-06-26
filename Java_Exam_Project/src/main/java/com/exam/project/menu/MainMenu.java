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
 * MainMenu - Manages the main game menu
 */
public class MainMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Builds and returns the main menu
     */
    public static GameMenu buildMainMenu() {
        try {
            GameMenu mainMenu = new GameMenu("RPG Adventure Game - Menu Principale");
            mainMenu.add(new MenuItem("Crea nuovo personaggio", MainMenu::createNewCharacter));
            mainMenu.add(new MenuItem("Carica Personaggio", MainMenu::loadCharacter));
            mainMenu.add(new MenuItem("Esci", MainMenu::exitGame));
            return mainMenu;
        } catch (Exception e) {
            logger.severe("Error building main menu: " + e.getMessage());
            GameMenu emergency = new GameMenu("Emergency Menu");
            emergency.add(new MenuItem("Esci", () -> System.exit(0)));
            return emergency;
        }
    }

    /**
     * Creates a new character and navigates to character menu
     */
    private static void createNewCharacter() {
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
            CharacterMenu.showCharacterMenu(character);
        } else {
            System.out.println("Creazione fallita!");
        }
    }

    /**
     * Loads an existing character and navigates to character menu
     */
    private static void loadCharacter() {
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
                CharacterMenu.showCharacterMenu(loaded);
            }
        }
    }

    /**
     * Exits the game
     */
    private static void exitGame() {
        System.out.println("\nGrazie per aver giocato!");
        logger.info("Game exited by user");
        System.exit(0);
    }

    /**
     * Runs the main menu
     */
    public static void runMainMenu() {
        try {
            GameMenu mainMenu = buildMainMenu();
            mainMenu.execute();
            logger.info("User exited from main menu");
        } catch (Exception e) {
            logger.severe("Error running main menu: " + e.getMessage());
            throw e;
        }
    }
}