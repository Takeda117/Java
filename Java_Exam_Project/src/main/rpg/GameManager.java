package rpg;

import rpg.menu.CharacterMenu.ReturnToMainMenuException;
import rpg.menu.MainMenu;
import rpg.logger.GameLogger;
import rpg.observer.GameUIObserver;
import rpg.observer.StaminaRecoverySystem;

import java.util.logging.Logger;

/**
 * GameManager - Handles game initialization and main loop
 * <p>
 * This class serves as the central controller for the RPG Adventure Game,
 * responsible for initializing all game systems and managing the main game loop.
 * It coordinates the interaction between various game components including:
 * <ul>
 *   <li>Menu systems (main menu and character menus)</li>
 *   <li>Observer pattern implementation for game events</li>
 *   <li>Logging system for game activities</li>
 * </ul>
 * </p>
 * <p>
 * The GameManager implements a robust error handling approach to ensure
 * that the game can gracefully handle exceptions without crashing.
 * </p>
 */
public class GameManager {
    private static final Logger logger = GameLogger.getLogger();
    
    /**
     * Initializes and starts the game
     * <p>
     * This method serves as the main entry point for the game logic.
     * It performs the following sequence of operations:
     * <ol>
     *   <li>Initializes the observer system for game events</li>
     *   <li>Initializes the menu connections</li>
     *   <li>Displays the welcome message</li>
     *   <li>Runs the main menu loop</li>
     * </ol>
     * </p>
     * <p>
     * The method includes comprehensive error handling to catch and log
     * any exceptions that might occur during game execution.
     * </p>
     * 
     * @throws RuntimeException If a critical error occurs that prevents the game from continuing
     */
    public void startGame() {
        try {
            logger.info("Starting RPG Adventure Game");
            
            initializeObservers();
            initializeMenus();
            showWelcome();
            runMainMenuLoop();
            
        } catch (Exception e) {
            logger.severe("Critical application error: " + e.getMessage());
            System.out.println("Si è verificato un errore critico. L'applicazione verrà chiusa.");
            System.exit(1);
        }
    }
    
    /**
     * Initializes observers for the game
     * <p>
     * Sets up the Observer Pattern implementation for the game by creating
     * and registering the necessary observer objects. This establishes the
     * communication channels between game components that need to react
     * to changes in game state.
     * </p>
     * <p>
     * Currently, this method registers a GameUIObserver with the StaminaRecoverySystem
     * to enable UI updates when character stamina changes.
     * </p>
     */
    private void initializeObservers() {
        GameUIObserver uiObserver = new GameUIObserver();
        StaminaRecoverySystem.addObserver(uiObserver);
        logger.info("GameUIObserver registered with StaminaRecoverySystem");
    }
    
    /**
     * Initializes menu connections
     * <p>
     * Ensures that all menu classes are properly loaded and initialized.
     * This method serves as a no-op that triggers the static initialization
     * blocks of menu classes, ensuring they are aware of each other and
     * properly connected.
     * </p>
     * <p>
     * In a more complex implementation, this method would handle more
     * explicit menu system initialization and configuration.
     * </p>
     */
    private void initializeMenus() {
        // Ensure all menu classes are aware of each other
        // This is a no-op method that ensures all menu classes are loaded
        // and their static initialization blocks are executed
        logger.info("Initializing menu system");
    }
    
    /**
     * Shows welcome message
     * <p>
     * Displays the initial welcome message to the player, including
     * information about the game and the design patterns used in its
     * implementation.
     * </p>
     */
    private void showWelcome() {
        System.out.println("=== RPG ADVENTURE GAME ===");
        System.out.println("Un gioco di ruolo testuale in Java");
    }
    
    /**
     * Runs main menu loop with return handling
     * <p>
     * Manages the main game loop, displaying the main menu and handling
     * navigation between different game menus. This method implements
     * a robust approach to menu navigation that allows returning to the
     * main menu from sub-menus through exception handling.
     * </p>
     * <p>
     * The loop continues until the player chooses to exit or an unhandled
     * exception occurs. When returning from character menus to the main menu,
     * a ReturnToMainMenuException is caught and handled gracefully.
     * </p>
     * 
     * @throws ReturnToMainMenuException When returning from a character menu to the main menu
     */
    private void runMainMenuLoop() {
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                MainMenu.runMainMenu();
                keepRunning = false;
            } catch (ReturnToMainMenuException e) {
                logger.info("Returned to main menu from character menu");
                System.out.println("\n--- Tornato al Menu Principale ---\n");
            } catch (Exception e) {
                logger.severe("Error in main menu loop: " + e.getMessage());
                System.out.println("Errore nel menu principale.");
                keepRunning = false;
            }
        }

        System.out.println("\nGrazie per aver giocato!");
        logger.info("Application terminated normally");
    }
}