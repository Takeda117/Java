package com.exam.project;

import com.exam.project.logger.GameLogger;
import java.util.logging.Logger;

/**
 * Main application entry point
 * Serves only as the entry point to start the game
 */
public class App {
    private static final Logger logger = GameLogger.getLogger();

    public static void main(String[] args) {
        try {
            // Initialize and start the game
            GameManager gameManager = new GameManager();
            gameManager.startGame();
        } catch (Exception e) {
            // Final exception shield
            logger.severe("Unhandled exception in main: " + e.getMessage());
            System.out.println("Si è verificato un errore critico. L'applicazione verrà chiusa.");
            System.exit(1);
        }
    }
}
