package java.menu;


import java.composite.GameMenu;
import java.composite.MenuItem;
import java.factory.Character;
import java.factory.CharacterFactory;
import java.rpgIO.CharacterManagement;
import java.rpgSecurity.InputValidator;
import java.logger.GameLogger;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * MainMenu - Manages the main game menu
 * <p>
 * This class is responsible for building and executing the main menu of the game.
 * It provides options for creating new characters, loading existing characters,
 * and exiting the game. The class uses the Composite pattern through the GameMenu
 * and MenuItem classes to structure the menu hierarchy.
 * </p>
 * <p>
 * The MainMenu serves as the entry point for player interaction with the game,
 * and handles navigation to character-specific menus when a character is created
 * or loaded.
 * </p>
 */
public class MainMenu {
    private static final Logger logger = GameLogger.getLogger();
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Builds and returns the main menu
     * <p>
     * Creates a GameMenu instance with options for creating a new character,
     * loading an existing character, and exiting the game. Each option is
     * associated with the appropriate action method.
     * </p>
     * 
     * @return A configured GameMenu instance for the main menu
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
     * <p>
     * Prompts the user to select a character type and enter a name,
     * then creates a new character using the CharacterFactory.
     * If successful, it displays the character menu for the new character.
     * </p>
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
     * <p>
     * Displays a list of available save files and prompts the user to select one.
     * If a valid selection is made, it loads the character from the save file
     * and displays the character menu for the loaded character.
     * </p>
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
     * <p>
     * Displays a farewell message, logs the exit action, and terminates the application.
     * </p>
     */
    private static void exitGame() {
        System.out.println("\nGrazie per aver giocato!");
        logger.info("Game exited by user");
        System.exit(0);
    }

    /**
     * Runs the main menu
     * <p>
     * Builds the main menu and executes it, handling any exceptions that may occur.
     * This is the primary entry point for starting the game's menu system.
     * </p>
     * 
     * @throws RuntimeException if an unrecoverable error occurs while running the menu
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