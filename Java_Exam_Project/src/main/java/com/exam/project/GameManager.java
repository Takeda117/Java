package com.exam.project;

import com.exam.project.menu.CharacterMenu.ReturnToMainMenuException;
import com.exam.project.menu.MainMenu;
import com.exam.project.logger.GameLogger;
import com.exam.project.observer.GameUIObserver;
import com.exam.project.observer.StaminaRecoverySystem;

import java.util.logging.Logger;

/**
 * GameManager - Handles game initialization and main loop
 */
public class GameManager {
    private static final Logger logger = GameLogger.getLogger();
    
    /**
     * Initializes and starts the game
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
     */
    private void initializeObservers() {
        GameUIObserver uiObserver = new GameUIObserver();
        StaminaRecoverySystem.addObserver(uiObserver);
        logger.info("GameUIObserver registered with StaminaRecoverySystem");
    }
    
    /**
     * Initializes menu connections
     */
    private void initializeMenus() {
        // Ensure all menu classes are aware of each other
        // This is a no-op method that ensures all menu classes are loaded
        // and their static initialization blocks are executed
        logger.info("Initializing menu system");
    }
    
    /**
     * Shows welcome message
     */
    private void showWelcome() {
        System.out.println("=== RPG ADVENTURE GAME ===");
        System.out.println("Un gioco di ruolo testuale in Java");
        System.out.println("Usa Design Patterns: Factory, Composite, Iterator, Builder, Strategy");
    }
    
    /**
     * Runs main menu loop with return handling
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