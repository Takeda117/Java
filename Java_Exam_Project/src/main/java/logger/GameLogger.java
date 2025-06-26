package logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * GameLogger - Singleton pattern with essential crash protection
 */
public class GameLogger {

    private static final Logger logger = createLogger();

    /**
     * Private constructor - Singleton pattern
     */
    private GameLogger() {
    }

    /**
     * Creates and configures the logger with error handling
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
     * @return Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }
}