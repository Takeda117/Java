package com.exam.project;

import com.exam.project.composite.GameMenu;
import com.exam.project.menu.MainMenuBuilder;
import com.exam.project.menu.CharacterMenuBuilder.ReturnToMainMenuException;
import com.exam.project.logger.GameLogger;

import java.util.logging.Logger;

/**
 * Main application entry point
 * Implements the simplified menu system with proper navigation
 */
public class App {
    private static final Logger logger = GameLogger.getLogger();

    public static void main(String[] args) {
        try {
            logger.info("Starting RPG Adventure Game");

            showWelcome();
            runMainMenuLoop();

        } catch (Exception e) {
            // Exception shielding - don't show stack traces to users
            logger.severe("Critical application error: " + e.getMessage());
            System.out.println("Si è verificato un errore critico. L'applicazione verrà chiusa.");
            System.exit(1);
        }
    }

    /**
     * Show welcome message
     */
    private static void showWelcome() {
        System.out.println("=== RPG ADVENTURE GAME ===");
        System.out.println("Un gioco di ruolo testuale in Java");
        System.out.println("Usa Design Patterns: Factory, Composite, Iterator, Builder, Strategy%n");
    }

    /**
     * Main menu loop - handles returning from character menu
     */
    private static void runMainMenuLoop() {
        boolean keepRunning = true;

        while (keepRunning) {
            try {
                GameMenu mainMenu = MainMenuBuilder.buildMainMenu();
                mainMenu.execute();

                // If we reach here, user chose "Exit" from main menu
                keepRunning = false;

            } catch (ReturnToMainMenuException e) {
                // Character menu returned to main menu (save, game over, etc.)
                logger.info("Returned to main menu from character menu");
                System.out.println("\n--- Tornato al Menu Principale ---\n");
                // Continue the loop to show main menu again

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
