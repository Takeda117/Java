package rpg.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * GameLogger - Logging utility for the RPG game
 * <p>
 * This class implements the Singleton pattern to provide a centralized
 * logging facility for the entire application. It encapsulates Java's
 * built-in logging framework and provides a simplified interface for
 * game components to log events, errors, and debug information.
 * </p>
 * <p>
 * The logger is configured with error handling to ensure that even if
 * the logging setup fails, a working logger instance is still available.
 * This prevents logging issues from causing application failures.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 * private static final Logger logger = GameLogger.getLogger();
 * logger.info("Game component initialized");
 * </pre>
 * </p>
 */
public class GameLogger {

    private static final Logger logger = createLogger();

    /**
     * Private constructor - Singleton pattern
     * <p>
     * Prevents instantiation from outside the class, ensuring
     * only one logger instance exists throughout the application.
     * </p>
     */
    private GameLogger() {
    }

    /**
     * Creates and configures the logger with error handling
     * <p>
     * This method initializes a Java Logger instance with appropriate
     * handlers and formatters. It includes error handling to ensure that
     * even if the configuration fails, a working logger is returned.
     * </p>
     * 
     * @return Configured logger instance
     */
    private static Logger createLogger() {
        Logger gameLogger = Logger.getLogger("RPG_Game");

        try {
            gameLogger.setUseParentHandlers(false);

            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            handler.setFormatter(new SimpleFormatter());

            gameLogger.addHandler(handler);
            gameLogger.setLevel(Level.ALL);

        } catch (Exception e) {
            // Fallback: if handler setup fails, still return working logger
            System.err.println("Warning: Logger setup failed, using default configuration");
        }

        return gameLogger;
    }

    /**
     * Gets the singleton logger instance
     * <p>
     * This method provides access to the centralized logger instance
     * that should be used throughout the application for all logging needs.
     * </p>
     * 
     * @return Logger instance configured for the game
     */
    public static Logger getLogger() {
        return logger;
    }
}