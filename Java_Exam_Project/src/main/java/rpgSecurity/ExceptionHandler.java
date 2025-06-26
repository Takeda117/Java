package java.rpgSecurity;

import java.logger.GameLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * ExceptionHandler - Manages game exceptions securely
 * <p>
 * This class provides centralized exception handling for the game application.
 * It serves multiple purposes:
 * <ul>
 *   <li>Prevents technical error details from being exposed to users</li>
 *   <li>Logs errors to the application log for debugging purposes</li>
 *   <li>Displays user-friendly messages to players</li>
 * </ul>
 * </p>
 * <p>
 * By routing all exceptions through this handler, the application maintains
 * a consistent approach to error handling and ensures that sensitive
 * implementation details are not exposed in user-facing error messages.
 * </p>
 */
public class ExceptionHandler {

    private static final Logger logger = GameLogger.getLogger();

    /**
     * Handles a generic exception
     * <p>
     * Logs the complete error details to the application log for developers
     * while showing only a safe, user-friendly message to the player.
     * </p>
     * 
     * @param e The exception to handle
     * @param userMessage The user-friendly message to display
     */
    public static void handleException(Exception e, String userMessage) {
        // Log the complete error (for developers)
        logger.log(Level.SEVERE, "Error: " + e.getMessage(), e);

        // Show only a safe message to the user
        System.out.println(userMessage);
    }

    /**
     * Handles errors during save/load operations
     * <p>
     * Provides more specific user messages based on the type of exception
     * that occurred during file operations, while logging the complete
     * technical details.
     * </p>
     * 
     * @param e The exception that occurred during save/load
     */
    public static void handleSaveLoadError(Exception e) {
        logger.log(Level.SEVERE, "Save/load error: " + e.getMessage(), e);
        
        // Provide more specific messages based on exception type
        if (e instanceof FileNotFoundException) {
            System.out.println("File not found. Check the filename.");
        } else if (e instanceof IOException) {
            System.out.println("File access problem. Check permissions.");
        } else {
            System.out.println("Error during save/load. Please try again.");
        }
    }

    /**
     * Handles errors during character creation
     * <p>
     * Logs character creation errors and displays a user-friendly message
     * about the failure.
     * </p>
     * 
     * @param e The exception that occurred during character creation
     */
    public static void handleCharacterError(Exception e) {
        logger.log(Level.WARNING, "Character error: " + e.getMessage(), e);
        System.out.println("Error in character creation. Check the entered data.");
    }

    /**
     * Handles inventory-related errors
     * <p>
     * Logs inventory operation errors and displays a generic message
     * about the failure.
     * </p>
     * 
     * @param e The exception that occurred during inventory operations
     */
    public static void handleInventoryError(Exception e) {
        logger.log(Level.WARNING, "Inventory error: " + e.getMessage(), e);
        System.out.println("Inventory operation failed.");
    }

    /**
     * Logs an important game action
     * <p>
     * Records significant player actions in the application log for
     * auditing and debugging purposes.
     * </p>
     * 
     * @param action The action to log
     */
    public static void logAction(String action) {
        logger.log(Level.INFO, "Action: " + action);
    }
}